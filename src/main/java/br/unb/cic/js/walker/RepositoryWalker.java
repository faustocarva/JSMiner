package br.unb.cic.js.walker;

import br.unb.cic.js.date.Formatter;
import br.unb.cic.js.date.Interval;
import br.unb.cic.js.miner.JSParser;
import br.unb.cic.js.miner.JSVisitor;
import br.unb.cic.js.miner.metrics.Metric;
import br.unb.cic.js.miner.metrics.Profiler;
import br.unb.cic.js.miner.metrics.Summary;
import br.unb.cic.js.walker.rules.DirectoriesRule;
import lombok.Builder;
import lombok.val;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * This class represents a git project to be analyzed.
 */
@Builder
public final class RepositoryWalker {
	private final Logger logger = LoggerFactory.getLogger(RepositoryWalker.class);

	public final String project;
	public final Path path;
	public final Boolean merges;

	private final List<Summary> summaries = Collections.synchronizedList(new ArrayList<>());

	private Repository repository;

	/**
	 * Traverse the git project from an initial date to an end date.
	 *
	 * @param interval The delta date of the traversal
	 * @param steps    How many days should the traverse use to group a set of
	 *                 commits?
	 * @param threads  How many threads to use when analyzing a revision
	 * @throws Exception
	 */
	public List<Summary> traverse(final Interval interval, final int steps, final int threads) throws Exception {
		logger.info("{} -- processing project", project);

		repository = FileRepositoryBuilder.create(path.toAbsolutePath().resolve(".git").toFile());

		val head = RepositoryWalkerGit.head(repository);
		val revisions = RepositoryWalkerGit.revisions(repository,merges);

		val commits = new HashMap<Date, ObjectId>();
		val commitDates = new HashSet<Date>(); // Use a HashSet for unique dates

		Date previous = null;

		// fill the commits map with commits that will be analyzed given that they
		// belong to the defined interval
		for (val revision : revisions) {
			// val author = revision.getAuthorIdent();
			val commitTimeInSeconds = revision.getCommitTime();
			val current = new Date((long) commitTimeInSeconds * 1000);
			if (current.compareTo(interval.begin) >= 0 && current.compareTo(interval.end) <= 0) {
				// only add commits that fit the interval

				if (previous == null || Interval.diff(current, previous, Interval.Unit.Days) >= steps) {
					commitDates.add(current);
					previous = current;

					// add just the date are not added
					if (!commits.containsKey(current)) {
						commits.put(current, revision.toObjectId());
					}
				}
			}
		}

		List<Date> sortedCommitDates = new ArrayList<>(commitDates);
		Collections.sort(sortedCommitDates);

		var traversed = 0;

		val totalGroups = sortedCommitDates.size();
		val totalCommits = commits.size();

		logger.info("{} -- total of commits {} ", project, totalCommits);
		logger.info("{} -- number of commit {} ", project, totalGroups);

		val profiler = new Profiler();

		for (Date current : sortedCommitDates) {
			traversed++;

			profiler.start();

			val summary = collect(head, current, commits, threads);

			profiler.stop();

			logger.info("{} -- collected commit {} of {} (took {}ms to collect)", project, traversed, totalGroups,
					profiler.last());

			summaries.add(summary);
		}

		val average = profiler.average();
		val total = (double) profiler.total() / 1000.0;

		logger.info("{} -- finished, took {}ms in average to collect each commit and {}s in total", project,
				average, total);

		return summaries;
	}

	/**
	 * Traverse the git project to look for a given hash and then collect metrics
	 * about that specific point.
	 *
	 * @param interval The delta date of the traversal
	 * @param hash     The hash of a given revision
	 * @param threads  How many threads to use when analyzing a revision
	 * @return
	 * @throws Exception
	 */
	public List<Summary> traverse(final Interval interval, final String hash, final int threads) throws Exception {
		logger.info("{} -- processing project for a single revision", project);

		repository = FileRepositoryBuilder.create(path.toAbsolutePath().resolve(".git").toFile());

		val head = RepositoryWalkerGit.head(repository);
		val revisions = RepositoryWalkerGit.revisions(repository,merges);

		val commits = new HashMap<Date, ObjectId>();

		for (val revision : revisions) {
			val id = revision.toObjectId();
			val commit = repository.parseCommit(id).getId().toString().split(" ")[1];

			if (commit.equals(hash)) {
				val commitTimeInSeconds = revision.getCommitTime();
				val current = new Date((long) commitTimeInSeconds * 1000);

				commits.put(current, revision.toObjectId());
				break;
			}
		}

		val current = commits.keySet().stream().findFirst().get();

		// collect only one summary
		summaries.add(collect(head, current, commits, threads));

		return summaries;
	}

	/**
	 * Collect metrics about a given commit interval
	 */
	private Summary collect(ObjectId head, Date current, Map<Date, ObjectId> commits, int threads) {
		val id = commits.get(current);
		val summary = Summary.builder();

		val metrics = new ArrayList<Metric<?>>();

		metrics.add(Metric.builder().name("project").value(project).build());
		metrics.add(Metric.builder().name("date (dd-mm-yyyy)").value(Formatter.format.format(current)).build());

		var errors = new HashMap<String, String>();

		try (Git git = new Git(repository)) {
			val commit = repository.parseCommit(id).getId().toString().split(" ")[1];

			metrics.add(Metric.builder().name("revision").value(commit).build());

			git.reset().setMode(ResetCommand.ResetType.HARD).call();
			git.checkout().setName(id.getName()).call();

			val walker = Files.walk(path, FileVisitOption.FOLLOW_LINKS);
			val files = walker.collect(Collectors.toList()).stream().filter(DirectoriesRule::walk)
					.filter(Files::isRegularFile).filter(file -> file.toString().endsWith(".js"))
					.collect(Collectors.toList());

			walker.close();

			metrics.add(Metric.builder().name("files").value(files.size()).build());

			val parser = new JSParser();
			val visitor = new JSVisitor();

			val tasks = new ArrayList<Future<?>>(threads);
			val pool = Executors.newFixedThreadPool(threads);

			for (Path p : files) {
				Runnable task = () -> {
					try {
						val content = new String(Files.readAllBytes(p));
						val program = parser.parse(content);

						program.accept(visitor);

					} catch (Exception ex) {
						errors.put(p + "-" + commit, ex.getMessage());
					}
				};

				tasks.add(pool.submit(task));
			}

			for (val task : tasks) {
				task.get();
			}

			pool.shutdown();

			metrics.add(Metric.builder().name("async-declarations").value(visitor.getTotalAsyncDeclarations().get())
					.build());
			metrics.add(Metric.builder().name("await-declarations").value(visitor.getTotalAwaitDeclarations().get())
					.build());
			metrics.add(Metric.builder().name("const-declarations").value(visitor.getTotalConstDeclaration().get())
					.build());
			metrics.add(Metric.builder().name("class-declarations").value(visitor.getTotalClassDeclarations().get())
					.build());
			metrics.add(Metric.builder().name("arrow-function-declarations")
					.value(visitor.getTotalArrowDeclarations().get()).build());
			metrics.add(
					Metric.builder().name("let-declarations").value(visitor.getTotalLetDeclarations().get()).build());
			metrics.add(Metric.builder().name("export-declarations").value(visitor.getTotalExportDeclarations().get())
					.build());
			metrics.add(Metric.builder().name("yield-declarations").value(visitor.getTotalYieldDeclarations().get())
					.build());
			metrics.add(
					Metric.builder().name("import-statements").value(visitor.getTotalImportStatements().get()).build());
			metrics.add(
					Metric.builder().name("promise-declarations").value(visitor.getTotalNewPromises().get()).build());
			metrics.add(Metric.builder().name("promise-all-and-then")
					.value(visitor.getTotalPromiseAllAndThenIdiom().get()).build());
			metrics.add(Metric.builder().name("default-parameters").value(visitor.getTotalDefaultParameters().get())
					.build());
			metrics.add(Metric.builder().name("rest-statements").value(visitor.getTotalRestStatements().get()).build());
			metrics.add(
					Metric.builder().name("spread-arguments").value(visitor.getTotalSpreadArguments().get()).build());
			metrics.add(Metric.builder().name("array-destructuring").value(visitor.getTotalArrayDestructuring().get())
					.build());
			metrics.add(Metric.builder().name("object-destructuring").value(visitor.getTotalObjectDestructuring().get())
					.build());
			
			
			metrics.add(Metric.builder().name("optional-chain").value(visitor.getTotalOptionalChain().get()).build());
			metrics.add(Metric.builder().name("template-string-expressions").value(visitor.getTotalTemplateStringExpressions().get()).build());
			metrics.add(Metric.builder().name("object-properties").value(visitor.getTotalObjectProperties().get()).build());
			metrics.add(Metric.builder().name("regular-expressions").value(visitor.getTotalRegularExpressions().get()).build());
			
			
			metrics.add(Metric.builder().name("errors").value(errors.size()).build());
			metrics.add(Metric.builder().name("statements").value(visitor.getTotalStatements().get()).build());
			
			
			summary.date(current).revision(head.toString()).metrics(metrics).errors(errors);
		} catch (Exception ex) {
			val commit = commits.get(current).toString().split(" ")[1];

			logger.error("failed to collect data for project {} on revision: {}", project, commit);
			ex.printStackTrace();

			errors.put("exception", ex.getMessage());
		} finally {
			summary.date(current).revision(head.toString()).metrics(metrics).errors(errors);
		}

		return summary.build();
	}
}

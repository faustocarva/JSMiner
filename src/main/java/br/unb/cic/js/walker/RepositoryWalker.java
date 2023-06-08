package br.unb.cic.js.walker;

import br.unb.cic.js.date.Interval;
import br.unb.cic.js.miner.JSParser;
import br.unb.cic.js.miner.JSVisitor;
import br.unb.cic.js.miner.metrics.Metric;
import br.unb.cic.js.miner.metrics.Summary;
import lombok.Builder;
import lombok.val;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents a git project to be analyzed.
 */
@Builder
public class RepositoryWalker {

    private final Logger logger = LoggerFactory.getLogger(RepositoryWalker.class);

    public final String project;
    public final Path path;

    private Repository repository;

    private List<Summary> summaries;

    /**
     * Traverse the git project from an initial date to an end date.
     *
     * @param initial
     * @param end
     * @param steps
     * @throws Exception
     */
    public List<Summary> traverse(Date initial, Date end, int steps) throws Exception {
        logger.info("{} -- processing project", project);

        summaries = new ArrayList<>();

        repository = FileRepositoryBuilder.create(path.toAbsolutePath().resolve(".git").toFile());

        val git = new Git(repository);
        val head = repository.resolve(Constants.HEAD);

        Map<Boolean, AnyObjectId> branches = new HashMap<>();
        branches.put(repository.resolve("refs/heads/master") != null, repository.resolve("refs/heads/master"));
        branches.put(repository.resolve("refs/heads/main") != null, repository.resolve("refs/heads/main"));

        val branch = branches.get(true);

        HashMap<Date, ObjectId> commits = new HashMap<>();
        List<Date> commitDates = new ArrayList<>();

        // fill the commits map with commits that will be analyzed given that they belong to the defined interval
        for (RevCommit commit : git.log().add(branch).call()) {
            PersonIdent author = commit.getAuthorIdent();
            Date current = author.getWhen();

            if (current.compareTo(initial) >= 0 && current.compareTo(end) <= 0) {
                commitDates.add(current);
                commits.put(current, commit.toObjectId());
            }
        }

        Collections.sort(commitDates);

        logger.info("{} -- number of commits {} ", project, commits.size());

        long traversed = 0;
        long total = commitDates.size();

        Date previous = null;

        for (Date current : commitDates) {
            if (traversed % 500 == 0) {
                logger.info("{} -- visiting commit {} of {}", project, traversed, total);
            }
            traversed++;

            if (previous == null || (Interval.diff(previous, current, Interval.Unit.Days) >= steps)) {
                collect(head, current, commits);

                previous = current;
            }
        }

        git.close();

        return summaries;
    }

    /**
     * Collect metrics about a given commit interval
     */
    private void collect(ObjectId head, Date current, Map<Date, ObjectId> commits) {
        val start = System.currentTimeMillis();
        val id = commits.get(current);

        try (Git git = new Git(repository)) {
            val commit = repository.parseCommit(id);

            git.checkout().setName(id.getName()).call();

            val walker = Files.walk(path, FileVisitOption.FOLLOW_LINKS);
            val files = walker.collect(Collectors.toList())
                    .stream()
                    .filter(filepath -> {
                        val file = filepath.toFile();

                        val isDirectory = file.isDirectory();
                        val isJsFile = file.toString().endsWith(".js");

                        return !isDirectory && isJsFile;
                    }).collect(Collectors.toList());

            val parser = new JSParser();
            val visitor = new JSVisitor();

            var errors = new Vector<String>();

            val metrics = new ArrayList<Metric>();

            for (Path p : files) {
                try {
                    val file = p.toFile();

                    val content = new String(Files.readAllBytes(file.toPath()));
                    val program = parser.parse(content);

                    program.accept(visitor);

                    visitor.getTotalAsyncDeclarations();
                } catch (ParseCancellationException ex) {
                    errors.add(p.toString());
                }
            }

            metrics.add(Metric.builder().name("async-declarations").value(visitor.getTotalAsyncDeclarations()).build());
            metrics.add(Metric.builder().name("async-declarations").value(visitor.getTotalAsyncDeclarations()).build());
            metrics.add(Metric.builder().name("await-declarations").value(visitor.getTotalAwaitDeclarations()).build());
            metrics.add(Metric.builder().name("const-declarations").value(visitor.getTotalConstDeclaration()).build());
            metrics.add(Metric.builder().name("class-declarations").value(visitor.getTotalClassDeclarations()).build());
            metrics.add(Metric.builder().name("function-declarations").value(visitor.getTotalFunctionDeclarations()).build());
            metrics.add(Metric.builder().name("let-declarations").value(visitor.getTotalLetDeclarations()).build());
            metrics.add(Metric.builder().name("export-declarations").value(visitor.getTotalExportDeclarations()).build());
            metrics.add(Metric.builder().name("yield-declarations").value(visitor.getTotalYieldDeclarations()).build());
            metrics.add(Metric.builder().name("import-statements").value(visitor.getTotalImportStatements()).build());
            metrics.add(Metric.builder().name("promise-declarations").value(visitor.getTotalNewPromises()).build());
            metrics.add(Metric.builder().name("promise-all-and-then").value(visitor.getTotalPromiseAllAndThenIdiom()).build());
            metrics.add(Metric.builder().name("default-parameters").value(visitor.getTotalDefaultParameters()).build());
            metrics.add(Metric.builder().name("rest-statements").value(visitor.getTotalRestStatements()).build());
            metrics.add(Metric.builder().name("spread-arguments").value(visitor.getTotalSpreadArguments()).build());
            metrics.add(Metric.builder().name("array-destructuring").value(visitor.getTotalArrayDestructuring()).build());
            metrics.add(Metric.builder().name("object-destructuring").value(visitor.getTotalObjectDestructuring()).build());

            val summary = Summary.builder()
                             .date(current)
                             .revision(head.toString())
                             .metrics(metrics)
                             .build();

            summaries.add(summary);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

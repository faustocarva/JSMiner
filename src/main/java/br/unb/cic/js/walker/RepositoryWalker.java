package br.unb.cic.js.walker;

import br.unb.cic.js.date.Interval;
import lombok.Builder;
import lombok.val;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
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

    private final Repository repository;

    /**
     * Traverse the git project from an initial date to an end date.
     *
     * @param initial
     * @param end
     * @param steps
     * @throws Exception
     */
    public void traverse(Date initial, Date end, int steps) throws Exception {
        logger.info("{} -- processing project", project);

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
                        val isJsFile = file.toString().split("\\.")[1] == "js";

                        return !isDirectory && isJsFile;
                    }).collect(Collectors.toList());

            for (Path p : files) {
                val file = p.toFile();

                // TODO: apply parser and get analytics about it
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

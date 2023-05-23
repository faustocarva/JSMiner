package br.unb.cic.js.walker;

import br.unb.cic.js.date.Interval;
import lombok.Builder;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Builder
public class Walker {

    private static final Logger logger = LogManager.getLogger(Walker.class);
    public final String path;
    public final String project;
    public final int steps;
    public final int threads;
    public final Date initialDate;
    public final Date endDate;

    public void traverse() {
        logger.info("initializing git traversal");
        logger.info(
                "path: {}\nproject: {}\nsteps: {}\nthreads: {}\ninitial date: {}\nend date: {}",
                path,
                project,
                steps,
                threads,
                initialDate,
                endDate
        );

        val f = new File(path);
        val p = Path.of(path);

        try {
            if (f.exists() && f.isDirectory()) {
                List<Path> repositories = new ArrayList<>();

                // checking a file attribute to verify if it's a directory is slow, be careful with the amount of
                // folders you'll be checking against.

                if (project.isEmpty()) {
                    repositories.addAll(Files.find(p, 1, (path, attrs) -> {
                        val isDirectory = attrs.isDirectory();
                        val isGitDirectory = path.resolve(".git").toFile().isDirectory();

                        return isDirectory && isGitDirectory;
                    }).collect(Collectors.toList()));
                } else {
                    repositories.addAll(Files.find(p, 1, (path, attrs) -> {
                        val isEqualPath = path.toString().equals(project);
                        val isDirectory = attrs.isDirectory();
                        val isGitDirectory = path.resolve(".git").toFile().isDirectory();

                        return isEqualPath && isDirectory && isGitDirectory;
                    }).collect(Collectors.toList()));
                }

                if (repositories.isEmpty()) {
                    logger.info("couldn't find any git folder in {}", p);
                    return;
                }

                // create a report directory that will contain the results
                val r = Paths.get(p.toAbsolutePath().getParent().toString(), "js-miner-out");
                if (!r.toFile().exists()) {
                    Files.createDirectory(r);
                }

                val pool = Executors.newFixedThreadPool(threads);
                val tasks = new Vector<Future>();

                for (Path repositoryPath: repositories) {
                    logger.info("project: {}", repositoryPath);

                    val walker = RepositoryWalker.builder()
                            .path(repositoryPath)
                            .build();

                    val report = Paths.get(r.toString(), repositoryPath.getFileName().toString() + ".csv");
                    val interval = Interval.builder()
                            .begin(initialDate)
                            .end(endDate)
                            .build();

                    val task = RepositoryWalkerTask.builder()
                            .walker(walker)
                            .report(report)
                            .interval(interval)
                            .steps(steps)
                            .build();

                    tasks.add(pool.submit(task));
                }

                // wait for every task to finish
                for (Future task : tasks) {
                    task.get();
                }

                pool.shutdown();
            } else {
                logger.warn("path {} does not exist or isn't a directory", p);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch ( java.lang.InterruptedException | java.util.concurrent.ExecutionException ex) {
            logger.error("failed to execute a concurrent task");
            ex.printStackTrace();
        }
    }
}

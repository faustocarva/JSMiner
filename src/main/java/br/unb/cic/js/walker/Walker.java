package br.unb.cic.js.walker;

import lombok.Builder;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

                // checking a file attribute to see if it is a directory is slow, be careful with the amount of folders
                // you'll be checking against.

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

                for (Path projectPath: repositories) {
                    logger.info("project: {}", projectPath);
                }
            } else {
                logger.warn("path {} does not exist or isn't a directory", p);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

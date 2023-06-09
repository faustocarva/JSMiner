package br.unb.cic.js.walker;

import br.unb.cic.js.date.Interval;
import lombok.Builder;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Path;

@Builder
public class RepositoryWalkerTask implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    // The report is the csv file generated by the program after all observations of a given project are finished.
    public final Path report;

    public final RepositoryWalker walker;

    public final Interval interval;

    public final int steps;

    @Override
    public void run() {
        try {
            val summaries = walker.traverse(interval.begin, interval.end, steps);

            // write to CSV file code below

            report.toFile().createNewFile();

            val content = new StringBuilder(); 

            // project, date, revision, metrics...
            content.append(summaries.get(0).head())
                   .append("\n");
            
            summaries.forEach(s -> {
                        content.append(s.values())
                       .append("\n");
            });

            val writer = new BufferedWriter(new FileWriter(report.toFile()));

            writer.write(content.toString());
            writer.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

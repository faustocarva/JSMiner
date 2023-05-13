package br.unb.cic.js;

import br.unb.cic.js.walker.Walker;
import com.beust.jcommander.JCommander;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

public class App {

    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String args[]) {

        val arguments = new Args();

        JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(args);

        try {
            var initialDate = Formatter.format.parse("01-01-2010");
            var endDate = Formatter.format.parse(Date.from(Instant.now()).toString());

            if (!arguments.initialDate.isEmpty()) {
                initialDate = Formatter.format.parse(arguments.initialDate);
            }
            if (!arguments.endDate.isEmpty()) {
                endDate = Formatter.format.parse(arguments.endDate);
            }

            val walker = Walker.builder()
                    .path(arguments.directory)
                    .steps(arguments.steps)
                    .threads(arguments.threads)
                    .initialDate(initialDate)
                    .endDate(endDate)
                    .build();

        } catch(ParseException ex) {
            ex.printStackTrace();
        }
    }
}

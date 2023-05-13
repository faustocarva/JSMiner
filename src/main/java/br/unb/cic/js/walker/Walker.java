package br.unb.cic.js.walker;

import br.unb.cic.js.App;
import lombok.Builder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

@Builder
public class Walker {

    private static final Logger logger = LogManager.getLogger(Walker.class);
    public final String path;
    public final int steps;
    public final int threads;
    public final Date initialDate;
    public final Date endDate;

    public void walk() {
        // TODO: walk through the set of projects
        logger.info("{} {} {} {} {}", path, steps, threads, initialDate, endDate);
    }
}

package br.unb.cic.js.miner.metrics;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.val;
import lombok.var;

/**
 * 
 */
@Builder
public class Summary {

    private final String project; // summary project name
    private final Date date; // date of git commit
    private final String revision; // commit hash

    public final List<Metric> metrics;

    public String head() {
        var h = new StringBuilder();

        h.append("project,date,revision");

        metrics.stream()
               .forEach(m -> {
                    h.append(",").append(m.name);
               });

        return h.toString();
    }

    public String process() {
        val l = new StringBuilder();

        metrics.stream()
               .forEach(m -> {
                    l.append(",").append(m.value.toString());
                });

        return l.toString();
    }
}
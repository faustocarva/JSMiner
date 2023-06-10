package br.unb.cic.js.miner.metrics;

import lombok.Builder;
import lombok.val;

import java.util.Date;
import java.util.List;

/**
 * 
 */
@Builder
public class Summary {

    private final String project; // summary project name
    private final Date date; // date of git commit
    private final String revision; // commit hash

    public final List<Metric> metrics;

    /**
     * Returns a string containing the header for the CSV report with default fields
     * @return
     */
    public static String header() {
        val h = new StringBuilder();

        h.append("project,date,revision,files")
                .append(",async-declarations")
                .append(",await-declarations")
                .append(",const-declarations")
                .append(",class-declarations")
                .append(",function-declarations")
                .append(",let-declarations")
                .append(",export-declarations")
                .append(",yield-declarations")
                .append(",import-statements")
                .append(",promise-declarations")
                .append(",promise-all-and-then")
                .append(",default-parameters")
                .append(",rest-statements")
                .append(",spread-arguments")
                .append(",array-destructuring")
                .append(",object-destructuring\n");

        return h.toString();
    }

    public String values() {
        val l = new StringBuilder();

        metrics.forEach(m -> l.append(m.value.toString()).append(","));

        return l.toString();
    }
}
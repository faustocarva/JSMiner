package br.unb.cic.js.miner.metrics;

import lombok.Builder;
import lombok.val;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Summary represents a collection of metrics for a given project, at a given date and on a given revision.
 */
@Builder
public class Summary {

    private final String project; // summary project name
    private final Date date; // date of git commit
    private final String revision; // commit hash

    // Internal summary size (the number of columns in the resulting CSV file)
    private final Integer size = header().split(",").length;

    public List<Metric<?>> metrics;

    // Map of files to errors that occurred when parsing or visiting
    public final HashMap<String, String> errors;

    /**
     * Returns a string containing the header for the CSV report with default fields
     */
    public static String header() {
        val h = new StringBuilder();

        h.append("project,date,revision,files")
                .append(",async-declarations")
                .append(",await-declarations")
                .append(",const-declarations")
                .append(",class-declarations")
                .append(",arrow-function-declarations")
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
                .append(",object-destructuring")
                .append(",optional-chain")
                .append(",template-string-expressions")
                .append(",object-properties")
                .append(",regular-expressions")
                .append(",errors")
                .append(",statements\n");

        return h.toString();
    }

    public String values() {
        if (metrics == null) {
            return "";
        }

        val l = new StringBuilder();

        metrics.forEach(m -> l.append(m.value.toString()).append(","));

        return l.toString();
    }
}
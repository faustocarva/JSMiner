package br.unb.cic.js;

import com.beust.jcommander.Parameter;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

public class Args {
    @Parameter(names = {"-d", "--directory"}, description = "The path to a/set of git repository(ies) containing JS code")
    public String directory = "";
    @Parameter(names = {"-s", "--steps"}, description = "The delta interval through each commit")
    public Integer steps = 7;
    @Parameter(names = {"-t", "--threads"}, description = "How many threads to use when processing multiple projects")
    public Integer threads = 1;

    @Parameter(names = {"-id", "--initial-date"}, description = "When to start walking a project (dd-mm-yyyy)")
    public String initialDate;
    @Parameter(names = {"-ed", "--end-date"}, description = "When to stop walking the project (dd-mm-yyyy)")
    public String endDate;
}

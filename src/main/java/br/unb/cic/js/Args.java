package br.unb.cic.js;

import com.beust.jcommander.Parameter;

public class Args {
    @Parameter(names = {"-d", "--directory"}, required = true, description = "The path to a/set of git repository(ies) containing JS code")
    public String directory = "";

    @Parameter(names = {"-p", "--project"}, description = "Given the project flag it will only mine for that project")
    public String project = "";

    @Parameter(names = {"-s", "--steps"}, description = "The delta interval through each commit")
    public Integer steps = 1;

    @Parameter(names = {"-pt", "--project-threads"}, description = "How many threads to use when processing multiple projects")
    public Integer threadsProjects = 1;

    @Parameter(names = {"-ft", "--files-threads"}, description = "How many threads to use when analyzing a project (defaults to number of processors")
    public Integer threadsFiles = Runtime.getRuntime().availableProcessors();

    @Parameter(names = {"-id", "--initial-date"}, description = "When to start walking a project (dd-mm-yyyy)")
    public String initialDate = "25-06-2023";

    @Parameter(names = {"-ed", "--end-date"}, description = "When to stop walking the project (dd-mm-yyyy)")
    public String endDate = "28-06-2023";

    @Parameter(names = {"--hash"}, description = "A commit hash to be verified")
    public String[] hash = {};

    @Parameter(names = {"--merges"}, description = "List commits with/without merge commits")
    public Boolean merges = false;
    
    @Parameter(names = {"-h", "--help"}, help = true, description = "Show help")
    private boolean help;
}

package br.unb.cic.js.walker.rules;

import lombok.val;

import java.nio.file.Path;
import java.util.Arrays;

public class WalkerDirectoriesRule {
    /**
     * This method filters out directories that match with a list of words defined within. The directories contain
     * files that are not from the analyzed project but are 3rd party code.
     *
     * @param filepath The path of a given file
     * @return If that filepath should be collected for analysis.
     */
    public static boolean walk(Path filepath) {
        val file = filepath.toFile().toString();
        val directories = new String[]{"lib", "vendor", "node_modules", "libs", "coverage", "build", "bin", "stories", "dist", "external"};

        return Arrays.stream(directories).noneMatch(file::contains);
    }
}

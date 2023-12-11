package br.unb.cic.js.walker.rules;

import lombok.val;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class DirectoriesRuleTest {

    String[] files;

    @Before
    public void setup() {
        files = new String[]{
                "src/something.js",
                "lib/something_else.js",
                "vendor/other.js",
        };
    }

    @Test
    public void testMatch() {
        val paths = Arrays.stream(files)
                .map(Path::of)
                .filter(DirectoriesRule::walk)
                .collect(Collectors.toList());

        assertEquals(1, paths.size());
    }
}
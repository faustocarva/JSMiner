package br.unb.cic.js.miner;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JSParserTest {

    @Test
    public void testParser() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            String content = new String(Files.readAllBytes(Paths.get(classLoader.getResource("helloworld.js").toURI())));
            JSParser.parse(content);
            Assert.assertTrue(true);
        }
        catch(Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}

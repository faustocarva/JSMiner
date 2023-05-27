package br.unb.cic.js.miner;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class JSParserTest {

    private JSParser parser;

    @Before
    public void setUp() {
        parser = new JSParser();
    }

	@Ignore
    public void testParser() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            String content = new String(Files.readAllBytes(Paths.get(classLoader.getResource("helloworld.js").toURI())));
            JavaScriptParser.ProgramContext p = parser.parse(content);
            JSVisitor visitor = new JSVisitor();
            p.accept(visitor);
            assertTrue(visitor.getTotalFunctionDeclarations() > 0);
            assertNotNull(p);
        }
        catch(Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    
	@Ignore
    public void testAsync() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            String content = new String(Files.readAllBytes(Paths.get(classLoader.getResource("examples/AsyncAwait.js").toURI())));
            JavaScriptParser.ProgramContext p = parser.parse(content);
            JSVisitor visitor = new JSVisitor();
            p.accept(visitor);
            assertEquals(11, visitor.getTotalAsyncDeclarations());
        }
        catch(Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testAwait() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            String content = new String(Files.readAllBytes(Paths.get(classLoader.getResource("examples/AsyncAwait.js").toURI())));
            JavaScriptParser.ProgramContext p = parser.parse(content);
            JSVisitor visitor = new JSVisitor();
            p.accept(visitor);
            assertEquals(3, visitor.getTotalAwaitDeclarations());
        }
        catch(Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testLet() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            String content = new String(Files.readAllBytes(Paths.get(classLoader.getResource("examples/Scoping.js").toURI())));
            JavaScriptParser.ProgramContext p = parser.parse(content);
            JSVisitor visitor = new JSVisitor();
            p.accept(visitor);

        }
        catch(Exception e) {
            e.printStackTrace();
            fail();
        }
    }

	@Ignore
    public void testParserReact() throws Exception{
    	ClassLoader classLoader = getClass().getClassLoader();
        URI directoryPath = classLoader.getResource("examples").toURI();
        
        List<File> files = new ArrayList<>();

		Files.walk(Paths.get(directoryPath)).forEach(filePath -> {
            if (Files.isRegularFile(filePath) && filePath.toString().endsWith(".js")) {
            	files.add(filePath.toFile());
            }
        });
		
		assertFalse(files.isEmpty());

        int errors = 0;
		int totalFunctionDeclarations = 0;
		int totalAsyncDeclarations = 0;

        for (File file : files) {
			try {
	            String content = new String(Files.readAllBytes(file.toPath()));
	            JavaScriptParser.ProgramContext p = parser.parse(content);
	            assertNotNull(p);
	            JSVisitor visitor = new JSVisitor();
	            p.accept(visitor);
	            totalFunctionDeclarations += visitor.getTotalFunctionDeclarations();
	            totalAsyncDeclarations += visitor.getTotalAsyncDeclarations();
			} catch (Throwable e) {
				errors++;
			}
		}

        assertEquals(34, files.size());
        assertEquals(63, totalFunctionDeclarations);
        assertEquals(65, totalAsyncDeclarations);
        assertEquals(0, errors);
    }
}

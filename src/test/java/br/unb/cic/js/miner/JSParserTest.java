package br.unb.cic.js.miner;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JSParserTest {

	@Ignore
    public void testParser() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            String content = new String(Files.readAllBytes(Paths.get(classLoader.getResource("helloworld.js").toURI())));
            JavaScriptParser.ProgramContext p = JSParser.parse(content);
            JsVisitor jv = new JsVisitor();
            p.accept(jv);
            Assert.assertTrue(jv.getTotalFunctions() > 0);
            Assert.assertNotNull(p);
        }
        catch(Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
    
	@Ignore
    public void testAsync() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            String content = new String(Files.readAllBytes(Paths.get(classLoader.getResource("examples/AsyncAwait.js").toURI())));
            JavaScriptParser.ProgramContext p = JSParser.parse(content);
            JsVisitor jv = new JsVisitor();
            p.accept(jv);
            Assert.assertEquals(11, jv.getTotalAsyncs());
        }
        catch(Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
    
	@Test
    public void testParserReact() throws Exception{
    	ClassLoader classLoader = getClass().getClassLoader();
        URI directoryPath = classLoader.getResource("examples").toURI();
        
        List<File> files = new ArrayList<>();
		Files.walk(Paths.get(directoryPath)).forEach(filePath -> {
            if (Files.isRegularFile(filePath) && filePath.toString().endsWith(".js")) {
            	files.add(filePath.toFile());
            }
        });
		
		Assert.assertFalse(files.isEmpty());
		int errors = 0;
		int totalFunctions = 0;
		int totalAsyncs = 0;
		for (File file : files) {
			try {
	            String content = new String(Files.readAllBytes(file.toPath()));
	            JavaScriptParser.ProgramContext p = JSParser.parse(content);
	            Assert.assertNotNull(p);
	            JsVisitor jv = new JsVisitor();
	            p.accept(jv);
	            totalFunctions += jv.getTotalFunctions();
	            totalAsyncs += jv.getTotalAsyncs();
			} catch (Throwable e) {
				errors++;
			}
		}
		
		System.out.println("Errors: "+errors);
		System.out.println("Files: "+files.size());
		System.out.println("Funcs: "+totalFunctions);
		System.out.println("Asyncs: "+totalAsyncs);
    }
}

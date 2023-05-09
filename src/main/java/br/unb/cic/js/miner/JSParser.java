package br.unb.cic.js.miner;


import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class JSParser {
    public static JavaScriptParser.ProgramContext parse(String content) throws Exception {
    	CharStream codePointCharStream = CharStreams.fromString(content);
        JavaScriptLexer lexer = new JavaScriptLexer(codePointCharStream);
        JavaScriptParser parser = new JavaScriptParser(new CommonTokenStream(lexer));
        parser.addParseListener(new JavaScriptParserBaseListener());
        return parser.program();
    }
}

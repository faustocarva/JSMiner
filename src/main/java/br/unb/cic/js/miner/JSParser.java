package br.unb.cic.js.miner;


import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class JSParser {
    public static void parse(String content) throws Exception {
        ECMAScriptLexer lexer = new ECMAScriptLexer(new ANTLRInputStream(content));
        ECMAScriptParser parser = new ECMAScriptParser(new CommonTokenStream(lexer));

        parser.program();
    }
}

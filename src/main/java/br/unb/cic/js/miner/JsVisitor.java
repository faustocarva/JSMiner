package br.unb.cic.js.miner;

import br.unb.cic.js.miner.JavaScriptParser.AnonymousFunctionDeclContext;
import br.unb.cic.js.miner.JavaScriptParser.ArrowFunctionContext;
import br.unb.cic.js.miner.JavaScriptParser.ClassElementAssigmentContext;
import br.unb.cic.js.miner.JavaScriptParser.ClassElementMethodDefinitionContext;
import br.unb.cic.js.miner.JavaScriptParser.FunctionDeclarationContext;
import br.unb.cic.js.miner.JavaScriptParser.FunctionPropertyContext;
import br.unb.cic.js.miner.JavaScriptParser.IdentifierContext;

public class JsVisitor extends JavaScriptParserBaseVisitor<Void> {

	int totalFunctions = 0;
	int totalAsyncs = 0;

	@Override
	public Void visitFunctionDeclaration(FunctionDeclarationContext ctx) {
		totalFunctions++;
		if (ctx.Async() != null) {
//			System.out.println(ctx.getText());
			totalAsyncs++;
		}
		return super.visitFunctionDeclaration(ctx);
	}

	@Override
	public Void visitArrowFunction(ArrowFunctionContext ctx) {
		totalFunctions++;
		if (ctx.Async() != null) {
//			System.out.println(ctx.getText());
			totalAsyncs++;
		}
		return super.visitArrowFunction(ctx);
	}

	@Override
	public Void visitAnonymousFunctionDecl(AnonymousFunctionDeclContext ctx) {
		totalFunctions++;
		if (ctx.Async() != null) {
//			System.out.println(ctx.getText());
			totalAsyncs++;
		}
		return super.visitAnonymousFunctionDecl(ctx);
	}

	@Override
	public Void visitFunctionProperty(FunctionPropertyContext ctx) {
		if (ctx.Async() != null) {
//			System.out.println(ctx.getText());
			totalAsyncs++;
		}
		return super.visitFunctionProperty(ctx);
	}

	@Override
	public Void visitIdentifier(IdentifierContext ctx) {
		if (ctx.Async() != null) {
//			System.out.println(ctx.getText());
			totalAsyncs++;
		}
		return super.visitIdentifier(ctx);
	}
	
	@Override
	public Void visitClassElementMethodDefinition(ClassElementMethodDefinitionContext ctx) {
		if(ctx.Async() != null) {
//			System.out.println(ctx.getText());
			totalAsyncs++;
		}
		return super.visitClassElementMethodDefinition(ctx);
	}
	
	@Override
	public Void visitClassElementAssigment(ClassElementAssigmentContext ctx) {
		if(ctx.Async() != null) {
//			System.out.println(ctx.getText());
			totalAsyncs++;
		}
		return super.visitClassElementAssigment(ctx);
	}


	public int getTotalFunctions() {
		return totalFunctions;
	}

	public int getTotalAsyncs() {
		return totalAsyncs;
	}

}

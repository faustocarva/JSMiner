package br.unb.cic.js.miner;

import br.unb.cic.js.miner.JavaScriptParser.AnonymousFunctionDeclContext;
import br.unb.cic.js.miner.JavaScriptParser.ArrowFunctionContext;
import br.unb.cic.js.miner.JavaScriptParser.ClassElementAssigmentContext;
import br.unb.cic.js.miner.JavaScriptParser.ClassElementMethodDefinitionContext;
import br.unb.cic.js.miner.JavaScriptParser.FunctionDeclarationContext;
import br.unb.cic.js.miner.JavaScriptParser.FunctionPropertyContext;
import br.unb.cic.js.miner.JavaScriptParser.IdentifierContext;

public class JSVisitor extends JavaScriptParserBaseVisitor<Void> {

	int totalFunctionDeclarations = 0;
	int totalAsyncDeclarations = 0;

	@Override
	public Void visitFunctionDeclaration(FunctionDeclarationContext ctx) {
		totalFunctionDeclarations++;
		if (ctx.Async() != null) {
			totalAsyncDeclarations++;
		}
		return super.visitFunctionDeclaration(ctx);
	}

	@Override
	public Void visitArrowFunction(ArrowFunctionContext ctx) {
		totalFunctionDeclarations++;
		if (ctx.Async() != null) {
			totalAsyncDeclarations++;
		}
		return super.visitArrowFunction(ctx);
	}

	@Override
	public Void visitAnonymousFunctionDecl(AnonymousFunctionDeclContext ctx) {
		totalFunctionDeclarations++;
		if (ctx.Async() != null) {
			totalAsyncDeclarations++;
		}
		return super.visitAnonymousFunctionDecl(ctx);
	}

	@Override
	public Void visitFunctionProperty(FunctionPropertyContext ctx) {
		if (ctx.Async() != null) {
			totalAsyncDeclarations++;
		}
		return super.visitFunctionProperty(ctx);
	}

	@Override
	public Void visitIdentifier(IdentifierContext ctx) {
		if (ctx.Async() != null) {
			totalAsyncDeclarations++;
		}
		return super.visitIdentifier(ctx);
	}
	
	@Override
	public Void visitClassElementMethodDefinition(ClassElementMethodDefinitionContext ctx) {
		if(ctx.Async() != null) {
			totalAsyncDeclarations++;
		}
		return super.visitClassElementMethodDefinition(ctx);
	}
	
	@Override
	public Void visitClassElementAssigment(ClassElementAssigmentContext ctx) {
		if(ctx.Async() != null) {
			totalAsyncDeclarations++;
		}
		return super.visitClassElementAssigment(ctx);
	}


	public int getTotalFunctionDeclarations() {
		return totalFunctionDeclarations;
	}

	public int getTotalAsyncDeclarations() {
		return totalAsyncDeclarations;
	}

}

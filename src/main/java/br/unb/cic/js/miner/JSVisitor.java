package br.unb.cic.js.miner;

import br.unb.cic.js.miner.JavaScriptParser.*;
import lombok.Getter;

@Getter
public class JSVisitor extends JavaScriptParserBaseVisitor<Void> {

	int totalFunctionDeclarations = 0;
	int totalAsyncDeclarations = 0;
	int totalAwaitDeclarations = 0;
	int totalLetDeclarations = 0;
	int totalConstDeclaration = 0;
	int totalClassDeclarations = 0;
	int totalYieldDeclarations = 0;
	int totalExportDeclarations = 0;
	int totalImportStatements = 0;
	int totalRestStatements = 0;

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
	public Void visitYieldExpression(YieldExpressionContext ctx) {
		if (ctx.yieldStatement() != null) {
			totalYieldDeclarations++;
		}
		return super.visitYieldExpression(ctx);
	}

	@Override
	public Void visitClassDeclaration(ClassDeclarationContext ctx) {
		if (ctx.Class() != null) {
			totalClassDeclarations++;
		}
		return super.visitClassDeclaration(ctx);
	}

	@Override
	public Void visitExportDeclaration(ExportDeclarationContext ctx) {
		if (ctx.Export() != null) {
			totalExportDeclarations++;
		}
		return super.visitExportDeclaration(ctx);
	}

	@Override
	public Void visitImportExpression(ImportExpressionContext ctx) {
		if (ctx.Import() != null) {
			totalImportStatements++;
		}
		return super.visitImportExpression(ctx);
	}

	@Override
	public Void visitImportStatement(ImportStatementContext ctx) {
		if (ctx.Import() != null) {
			totalImportStatements++;
		}
		return super.visitImportStatement(ctx);
	}

	@Override
	public Void visitLastFormalParameterArg(LastFormalParameterArgContext ctx) {
		if (ctx.Ellipsis() != null) {
			totalRestStatements++;
		}
		return super.visitLastFormalParameterArg(ctx);
	}

	@Override
	public Void visitVarModifier(VarModifierContext ctx) {
		if (ctx.Const() != null) {
			totalConstDeclaration++;
		}
		if (ctx.let_() != null) {
			totalLetDeclarations++;
		}
		return super.visitVarModifier(ctx);
	}

	@Override
	public Void visitAwaitExpression(AwaitExpressionContext ctx) {
		if (ctx.Await() != null) {
			totalAwaitDeclarations++;
		}
		return super.visitAwaitExpression(ctx);
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

}

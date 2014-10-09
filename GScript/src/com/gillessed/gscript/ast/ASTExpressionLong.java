package com.gillessed.gscript.ast;

import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.Token;

public class ASTExpressionLong extends ASTExpression {
	
	private Token token;

	public ASTExpressionLong(List<AbstractSyntaxTree> tokens) {
		this.token = (Token)tokens.get(0);
	}
	
	@Override
	public GObject run(Environment env) {
		return new GObject(Long.parseLong(token.getValue()), "Long");
	}
}

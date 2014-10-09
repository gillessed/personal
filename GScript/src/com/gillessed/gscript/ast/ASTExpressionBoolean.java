package com.gillessed.gscript.ast;

import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.Token;

public class ASTExpressionBoolean extends ASTExpression {
	
	private Token token;

	public ASTExpressionBoolean(List<AbstractSyntaxTree> tokens) {
		this.token = (Token)tokens.get(0);
	}
	
	@Override
	public GObject run(Environment env) {
		return new GObject(Boolean.parseBoolean(token.getValue()), "Boolean");
	}
}

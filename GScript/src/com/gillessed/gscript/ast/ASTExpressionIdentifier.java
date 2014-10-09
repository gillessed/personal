package com.gillessed.gscript.ast;

import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.Token;

public class ASTExpressionIdentifier extends ASTExpression {
	
	private Token token;

	public ASTExpressionIdentifier(List<AbstractSyntaxTree> tokens) {
		this.token = (Token)tokens.get(0);
	}
	
	@Override
	public GObject run(Environment env) {
		return env.getValueForIdentifier(token.getValue());
	}
}
package com.gillessed.gscript.ast;

import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.Token;

public class ASTStatement extends AbstractSyntaxTree {
	
	private final String identifier;
	private final ASTExpression expression;
	
	public ASTStatement(List<AbstractSyntaxTree> tokens) {
		identifier = ((Token)tokens.get(0)).getValue();
		expression = ((ASTExpression)tokens.get(2));
	}

	@Override
	public GObject run(Environment env) {
		env.setValueForIdentifier(identifier, expression.run(env));
		return null;
	}
}
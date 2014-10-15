package com.gillessed.gscript.ast;

import java.math.BigDecimal;
import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.Token;
import com.gillessed.gscript.GObject.Type;

public class ASTExpressionFloat extends ASTExpression {
	
	protected Token token;

	public ASTExpressionFloat(List<AbstractSyntaxTree> tokens) {
		this.token = (Token)tokens.get(0);
	}
	
	@Override
	public GObject run(Environment env, ASTFunction function) {
		return new GObject(new BigDecimal(token.getValue()), Type.FLOAT);
	}
}

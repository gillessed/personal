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
        super(tokens.get(0).getLineNumber());
		this.token = (Token)tokens.get(0);
	}
	
	@Override
	public GObject run(Environment env, ASTFunction function) {
	    BigDecimal value = new BigDecimal(token.getValue()).setScale(16);
		return new GObject(value, Type.FLOAT);
	}
}

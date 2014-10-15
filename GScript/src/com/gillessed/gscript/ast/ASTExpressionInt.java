package com.gillessed.gscript.ast;

import java.math.BigInteger;
import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GObject.Type;

public class ASTExpressionInt extends ASTExpressionFloat {
	
	public ASTExpressionInt(List<AbstractSyntaxTree> tokens) {
	    super(tokens);
	}
	
	@Override
	public GObject run(Environment env, ASTFunction function) {
		return new GObject(new BigInteger(token.getValue()), Type.INT);
	}
}

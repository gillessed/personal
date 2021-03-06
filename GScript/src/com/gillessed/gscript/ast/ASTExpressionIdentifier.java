package com.gillessed.gscript.ast;

import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GScriptException;
import com.gillessed.gscript.Token;

public class ASTExpressionIdentifier extends ASTExpression {
	
	private Token token;

	public ASTExpressionIdentifier(List<AbstractSyntaxTree> tokens) {
        super(tokens.get(0).getLineNumber());
		this.token = (Token)tokens.get(0);
	}
	
	@Override
	public GObject run(Environment env, ASTFunction function) throws GScriptException {
		return env.getValueForIdentifier(token.getValue());
	}

    public Token getToken() {
        return token;
    }
}

package com.gillessed.gscript.ast;

import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GScriptException;

public class ASTExpressionParen extends ASTExpression {

    private ASTExpression expression;

    public ASTExpressionParen(List<AbstractSyntaxTree> tokens) {
        super(tokens.get(1).getLineNumber());
        this.expression = (ASTExpression)tokens.get(1);
    }
    
    @Override
    public GObject run(Environment env, ASTFunction function) throws GScriptException {
        return expression.run(env, null);
    }
}

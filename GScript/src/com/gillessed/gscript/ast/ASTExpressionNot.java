package com.gillessed.gscript.ast;

import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GObject.Type;
import com.gillessed.gscript.GScriptException;

public class ASTExpressionNot extends ASTExpression {

    private ASTExpression expression;
    
    public ASTExpressionNot(List<AbstractSyntaxTree> tokens) {
        super(tokens.get(0).getLineNumber());
        expression = (ASTExpression)tokens.get(1);
    }
    
    @Override
    public GObject run(Environment env, ASTFunction function) throws GScriptException {
        Environment current = env.push();
        GObject result = expression.run(current, function);
        if(result.getType() != Type.BOOL) {
            throw new GScriptException("Non-boolean type for if-condition", getLineNumber());
        }
        boolean boolResult = (Boolean)result.getValue();
        return new GObject(!boolResult, Type.BOOL);
    }
}

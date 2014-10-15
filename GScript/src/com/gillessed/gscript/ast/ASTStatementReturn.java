package com.gillessed.gscript.ast;

import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GObject.Type;
import com.gillessed.gscript.GScriptException;

public class ASTStatementReturn extends ASTStatement {
    
    private ASTExpression expression;
    
    public ASTStatementReturn(List<? extends AbstractSyntaxTree> tokens) {
        expression = (ASTExpression)tokens.get(1);
    }
    
    @Override
    public GObject run(Environment env, ASTFunction function) throws GScriptException {
        GObject result = expression.run(env, function);
        function.finished(result);
        return new GObject(null, Type.VOID);
    }
}

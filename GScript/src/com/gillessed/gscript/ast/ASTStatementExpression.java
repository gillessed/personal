package com.gillessed.gscript.ast;

import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GObject.Type;
import com.gillessed.gscript.GScriptException;

public class ASTStatementExpression extends ASTStatement {
    
    private final ASTExpression expression;
    
    public ASTStatementExpression(List<AbstractSyntaxTree> tokens) {
        expression = ((ASTExpression)tokens.get(0));
    }

    @Override
    public GObject run(Environment env, ASTFunction function) throws GScriptException {
        expression.run(env, function);
        return new GObject(null, Type.VOID);
    }

}

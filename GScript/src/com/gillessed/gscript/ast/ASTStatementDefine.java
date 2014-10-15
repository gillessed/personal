package com.gillessed.gscript.ast;

import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GScriptException;
import com.gillessed.gscript.Token;

public class ASTStatementDefine extends ASTStatement {
    
    private final String identifier;
    private final ASTExpression expression;
    
    public ASTStatementDefine(List<AbstractSyntaxTree> tokens) {
        identifier = ((Token)tokens.get(0)).getValue();
        expression = ((ASTExpression)tokens.get(2));
    }

    @Override
    public GObject run(Environment env, ASTFunction function) throws GScriptException {
        env.setValueForIdentifier(identifier, expression.run(env, function));
        return null;
    }
}

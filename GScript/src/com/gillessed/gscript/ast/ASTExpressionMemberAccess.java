package com.gillessed.gscript.ast;

import java.lang.reflect.Field;
import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GObjectConverter;
import com.gillessed.gscript.GScriptException;
import com.gillessed.gscript.Token;

public class ASTExpressionMemberAccess extends ASTExpression {
    
    private ASTExpression object;
    private String member;
    
    public ASTExpressionMemberAccess(List<AbstractSyntaxTree> tokens) {
        super(tokens.get(2).getLineNumber());
        object = (ASTExpression)tokens.get(0);
        member = ((Token)tokens.get(2)).getValue();
    }

    @Override
    public GObject run(Environment env, ASTFunction function) throws GScriptException {
        Object javaObject = object.run(env, function).getValue();
        if(javaObject == null) {
            throw new GScriptException("Null pointer exception", getLineNumber());
        }
        Object result = null;
        try {
            Field field = javaObject.getClass().getField(member);
            result = field.get(javaObject);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new GScriptException("No such member " + member, getLineNumber());
        }
        return GObjectConverter.convertToGObject(result);
    }
}

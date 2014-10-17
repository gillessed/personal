package com.gillessed.gscript.ast;

import java.math.BigInteger;
import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GObject.Type;
import com.gillessed.gscript.GScriptException;

public class ASTExpressionArrayAccess extends ASTExpression {

    private ASTExpression array;
    private ASTExpression index;
    
    public ASTExpressionArrayAccess(List<? extends AbstractSyntaxTree> tokens) {
        super(tokens.get(1).getLineNumber());
        array = (ASTExpression)tokens.get(0);
        index = (ASTExpression)tokens.get(2);
    }

    @Override
    public GObject run(Environment env, ASTFunction function) throws GScriptException {
        GObject arrayResult = array.run(env, function);
        if(arrayResult.getType() != Type.LIST) {
            throw new GScriptException("Accessing object is not a list", getLineNumber());
        }
        GObject[] list = (GObject[])arrayResult.getValue();
        
        GObject indexResult = index.run(env, function);
        if(indexResult.getType() != Type.INT) {
            throw new GScriptException("Index is not an int", getLineNumber());
        }
        int actualIndex = ((BigInteger)indexResult.getValue()).intValue();
        if(actualIndex < 0 || actualIndex >= list.length) {
            throw new GScriptException("Index out of bounds: " + actualIndex + " for length " + list.length, getLineNumber());
        }
        return list[actualIndex];
    }
}

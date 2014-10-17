package com.gillessed.gscript.ast;

import java.util.ArrayList;
import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.FunctionKey;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GObject.Type;
import com.gillessed.gscript.GScriptException;
import com.gillessed.gscript.Token;

public class ASTExpressionFunctionCall extends ASTExpression {
    
    private String functionName;
    private List<ASTExpression> arguments;
    
    public ASTExpressionFunctionCall(List<? extends AbstractSyntaxTree> tokens) {
        super(tokens.get(0).getLineNumber());
        functionName = ((Token)tokens.get(0)).getValue();
        arguments = new ArrayList<>();
        int index = 2;
        while(index < tokens.size() && tokens.get(index).getParseType().isSubtypeOf(ASTExpression.class)) {
            arguments.add((ASTExpression)tokens.get(index));
            index+= 2;
        }
    }

    @Override
    public GObject run(Environment env, ASTFunction function) throws GScriptException {
        GObject functionObject = env.getFunctionForKey(new FunctionKey(functionName, arguments.size()));
        if(functionObject.getType() != Type.FUNCTION) {
            throw new RuntimeException("GObject is of type " + functionObject.getType() + " and so is not a function.");
        }
        ASTFunction functionToRun = (ASTFunction)functionObject.getValue();
        List<GObject> calculatedArguments = new ArrayList<GObject>();
        for(ASTExpression expression : arguments) {
            calculatedArguments.add(expression.run(env, function));
        }
        return functionToRun.run(env, calculatedArguments);
    }
}

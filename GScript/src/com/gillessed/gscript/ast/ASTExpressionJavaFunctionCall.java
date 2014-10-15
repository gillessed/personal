package com.gillessed.gscript.ast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GObjectConverter;
import com.gillessed.gscript.GScriptException;
import com.gillessed.gscript.Token;

public class ASTExpressionJavaFunctionCall extends ASTExpression {

    private ASTExpression target;
    private String functionName;
    private List<ASTExpression> arguments;
    public ASTExpressionJavaFunctionCall(List<? extends AbstractSyntaxTree> tokens) {
        target = (ASTExpression)tokens.get(0);
        functionName = ((Token)tokens.get(2)).getValue();
        arguments = new ArrayList<>();
        int index = 4;
        while(index < tokens.size() && tokens.get(index).getParseType().isSubtypeOf(ASTExpression.class)) {
            arguments.add((ASTExpression)tokens.get(index));
            index+= 2;
        }
    }
    @Override
    public GObject run(Environment env, ASTFunction function) throws GScriptException {
        GObject functionObject = target.run(env, function);
        Object functionJavaObject = functionObject.getValue();
        if(functionJavaObject == null) {
            throw new GScriptException("Null pointer exception.");
        }
        List<GObject> calculatedArguments = new ArrayList<GObject>();
        for(ASTExpression expression : arguments) {
            calculatedArguments.add(expression.run(env, function));
        }
        Object[] javaArguments = new Object[calculatedArguments.size()];
        for(int i = 0; i < javaArguments.length; i++) {
            javaArguments[i] = GObjectConverter.convertFromGObject(calculatedArguments.get(i));
        }
        Class<?>[] parameterTypes = new Class<?>[javaArguments.length];
        Method method;
        Object result = null;
        try {
            Class<?> clazz = functionJavaObject.getClass();
            method = clazz.getMethod(functionName, parameterTypes);
            result = method.invoke(functionJavaObject, javaArguments);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new GScriptException("No such method: " + functionName);
        }
        return GObjectConverter.convertToGObject(result);
    }
}

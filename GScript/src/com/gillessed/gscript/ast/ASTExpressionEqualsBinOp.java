package com.gillessed.gscript.ast;

import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GObject.Type;
import com.gillessed.gscript.GScriptException;
import com.gillessed.gscript.Token;
import com.gillessed.gscript.TokenType;

public class ASTExpressionEqualsBinOp extends ASTExpression {

    private ASTExpression left;
    private Token operator;
    private ASTExpression right;
    private boolean not;
    
    public ASTExpressionEqualsBinOp(List<AbstractSyntaxTree> tokens) {
        super(tokens.get(1).getLineNumber());
        this.left = (ASTExpression)tokens.get(0);
        this.operator = (Token)tokens.get(1);
        TokenType operatorType = operator.getTokenType();
        if(operatorType == TokenType.EQUAL) {
            not = false;
        } else if(operatorType == TokenType.NEQUAL) {
            not = true;
        } else {
            throw new RuntimeException("Incorrect equals operator." +
                    "\tLine " + operator.getLineNumber());
        }
        this.right = (ASTExpression)tokens.get(2);
    }
    
    @Override
    public GObject run(Environment env, ASTFunction function) throws GScriptException {
        GObject leftValue = left.run(env, null);
        GObject rightValue = right.run(env, null);
        boolean result = compare(leftValue, rightValue);
        return new GObject(not ^ result, Type.BOOL);
    }
    
    public boolean compare(GObject leftValue, GObject rightValue) throws GScriptException {
        boolean result;
        if(leftValue.getType() == Type.INT && rightValue.getType() == Type.INT) {
            result = leftValue.equals(rightValue);
        } else if(leftValue.getType() == Type.FLOAT && rightValue.getType() == Type.FLOAT) {
            result = leftValue.equals(rightValue);
        } else if(leftValue.getType() == Type.BOOL && rightValue.getType() == Type.BOOL) {
            result = leftValue.equals(rightValue);
        } else if(leftValue.getType() == Type.CHAR && rightValue.getType() == Type.CHAR) {
            result = leftValue.equals(rightValue);
        } else {
           result = leftValue.getType() == rightValue.getType() && leftValue.getValue() == rightValue.getValue();
        }
        return result;
    }
    
}

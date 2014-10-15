package com.gillessed.gscript.ast;

import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GObject.Type;
import com.gillessed.gscript.GScriptException;
import com.gillessed.gscript.Token;
import com.gillessed.gscript.TokenType;

public class ASTExpressionBoolBinOp extends ASTExpression {

    private ASTExpression left;
    private Token operator;
    private ASTExpression right;
    private TokenType operatorType;
    
    public ASTExpressionBoolBinOp(List<AbstractSyntaxTree> tokens) {
        this.left = (ASTExpression)tokens.get(0);
        this.operator = (Token)tokens.get(1);
        operatorType = operator.getTokenType();
        this.right = (ASTExpression)tokens.get(2);
    }
    
    @Override
    public GObject run(Environment env, ASTFunction function) throws GScriptException {
        GObject leftValue = left.run(env, null);
        GObject rightValue = right.run(env, null);
        boolean leftBool = (boolean)(leftValue.getValue());
        boolean rightBool = (boolean)(rightValue.getValue());
        if(leftValue.getType() == Type.BOOL && rightValue.getType() == Type.BOOL) {
            leftBool = (boolean)(leftValue.getValue());
            rightBool = (boolean)(rightValue.getValue());
        } else {
            throw new GScriptException("Operator " + operator.getTokenType() + " cannot be applied " + 
                "to types " + leftValue.getType() + " and " + rightValue.getType());
        }
        boolean result;
        switch(operatorType) {
        case OR:
            result = leftBool || rightBool;
        break;
        case AND:
            result = leftBool && rightBool;
        break;
        case XOR:
            result = leftBool ^ rightBool;
        break;
        default:
            throw new GScriptException("Unknown operator: " + operator.getTokenType());
        }
        return new GObject(result, Type.BOOL);
    }
    
}

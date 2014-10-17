package com.gillessed.gscript.ast;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GObject.Type;
import com.gillessed.gscript.GScriptException;
import com.gillessed.gscript.Token;
import com.gillessed.gscript.TokenType;

public class ASTExpressionCompBinOp extends ASTExpression {

    private ASTExpression left;
    private Token operator;
    private ASTExpression right;
    private TokenType operatorType;
    
    public ASTExpressionCompBinOp(List<AbstractSyntaxTree> tokens) {
        super(tokens.get(1).getLineNumber());
        this.left = (ASTExpression)tokens.get(0);
        this.operator = (Token)tokens.get(1);
        operatorType = operator.getTokenType();
        this.right = (ASTExpression)tokens.get(2);
    }
    
    @Override
    public GObject run(Environment env, ASTFunction function) throws GScriptException {
        GObject leftValue = left.run(env, null);
        GObject rightValue = right.run(env, null);
        int comparison;
        if(leftValue.getType() == Type.INT && rightValue.getType() == Type.INT) {
            BigInteger leftNum = (BigInteger)(leftValue.getValue());
            BigInteger rightNum = (BigInteger)(rightValue.getValue());
            comparison = leftNum.compareTo(rightNum);
        } else if(leftValue.getType() == Type.FLOAT && rightValue.getType() == Type.INT) {
            BigInteger leftNumInt = (BigInteger)(leftValue.getValue());
            BigDecimal leftNum = new BigDecimal(leftNumInt);
            BigDecimal rightNum = (BigDecimal)(rightValue.getValue());
            comparison = leftNum.compareTo(rightNum);
        } else if(leftValue.getType() == Type.INT && rightValue.getType() == Type.FLOAT) {
            BigDecimal leftNum = (BigDecimal)(leftValue.getValue());
            BigInteger rightNumInt = (BigInteger)(rightValue.getValue());
            BigDecimal rightNum = new BigDecimal(rightNumInt);
            comparison = leftNum.compareTo(rightNum);
        } else if(leftValue.getType() == Type.FLOAT && rightValue.getType() == Type.FLOAT) {
            BigDecimal leftNum = (BigDecimal)(leftValue.getValue());
            BigDecimal rightNum = (BigDecimal)(rightValue.getValue());
            comparison = leftNum.compareTo(rightNum);
        } else {
            throw new GScriptException("Operator " + operator.getTokenType() + " cannot be applied " + 
                "to types " + leftValue.getType() + " and " + rightValue.getType(), getLineNumber());
        }
        boolean result;
        switch(operatorType) {
        case GT:
            result = (comparison == 1);
        break;
        case GTE:
            result = (comparison == 1 || comparison == 0);
        break;
        case LT:
            result = (comparison == -1);
        break;
        case LTE:
            result = (comparison == -1 || comparison == 0);
        break;
        default:
            throw new GScriptException("Unknown operator: " + operator.getTokenType(), getLineNumber());
        }
        return new GObject(result, Type.BOOL);
    }
    
}

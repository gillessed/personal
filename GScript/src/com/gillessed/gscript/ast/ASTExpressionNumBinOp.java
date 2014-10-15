package com.gillessed.gscript.ast;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.gillessed.gscript.Environment;
import com.gillessed.gscript.GObject;
import com.gillessed.gscript.GObject.Type;
import com.gillessed.gscript.GScriptException;
import com.gillessed.gscript.Token;


public class ASTExpressionNumBinOp extends ASTExpression {
    
    public ASTExpression left;
    public Token operator;
    public ASTExpression right;
    
    public ASTExpressionNumBinOp(List<AbstractSyntaxTree> tokens) {
        this.left = (ASTExpression)tokens.get(0);
        this.operator = (Token)tokens.get(1);
        this.right = (ASTExpression)tokens.get(2);
    }
    
    @Override
    public GObject run(Environment env, ASTFunction function) throws GScriptException {
        GObject leftValue = left.run(env, null);
        GObject rightValue = right.run(env, null);
        if(leftValue.getType() == Type.INT) {
            if(rightValue.getType() == Type.INT) {
                return runIntInt((BigInteger)leftValue.getValue(), (BigInteger)rightValue.getValue(), env);
            } else if(rightValue.getType() == Type.FLOAT) {
                BigInteger leftInt = (BigInteger)leftValue.getValue();
                BigDecimal leftFloat = new BigDecimal(leftInt);
                BigDecimal rightFloat = (BigDecimal)rightValue.getValue();
                return runFloatFloat(leftFloat, rightFloat, env);
            }
        } else if(leftValue.getType() == Type.FLOAT) {
            if(rightValue.getType() == Type.INT) { 
                BigDecimal leftFloat = (BigDecimal)leftValue.getValue();
                BigInteger rightInt = (BigInteger)rightValue.getValue();
                BigDecimal rightFloat = new BigDecimal(rightInt);
                return runFloatFloat(leftFloat, rightFloat, env);
            } else if(rightValue.getType() == Type.FLOAT) {
                return runFloatFloat((BigDecimal)leftValue.getValue(), (BigDecimal)rightValue.getValue(), env);
            }
        }
        throw new GScriptException("Operator " + operator.getTokenType() + " cannot be applied " + 
            "to types " + leftValue.getType() + " and " + rightValue.getType());
    }

    public GObject runIntInt(BigInteger left, BigInteger right, Environment env) throws GScriptException {
        switch(operator.getTokenType()) {
        case PLUS:
            return new GObject(left.add(right), Type.INT);
        case MINUS:
            return new GObject(left.subtract(right), Type.INT);
        case TIMES:
            return new GObject(left.multiply(right), Type.INT);
        case DIVIDE:
            return new GObject(left.divide(right), Type.INT);
        case MOD:
            return new GObject(left.remainder(right), Type.INT);
        case EXP:
            return new GObject(left.pow(right.intValue()), Type.INT);
        case LOG:
            // TODO
            throw new GScriptException("Logarithm is not implemented yet.");
        default:
            throw new GScriptException("Unknown operator: " + operator.getTokenType());
        }
    }

    public GObject runFloatFloat(BigDecimal left, BigDecimal right, Environment env) throws GScriptException {
        switch(operator.getTokenType()) {
        case PLUS:
            return new GObject(left.add(right), Type.FLOAT);
        case MINUS:
            return new GObject(left.subtract(right), Type.FLOAT);
        case TIMES:
            return new GObject(left.multiply(right), Type.FLOAT);
        case DIVIDE:
            return new GObject(left.divide(right), Type.FLOAT);
        case MOD:
            return new GObject(left.remainder(right), Type.FLOAT);
        case EXP:
            return new GObject(left.pow(right.intValue()), Type.FLOAT);
        case LOG:
            // TODO
            throw new GScriptException("Logarithm is not implemented yet.");
        default:
            throw new GScriptException("Unknown operator: " + operator.getTokenType());
        }
    }
}

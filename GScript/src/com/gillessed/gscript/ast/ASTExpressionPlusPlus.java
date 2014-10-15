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

public class ASTExpressionPlusPlus extends ASTExpression {
    
    private ASTExpressionIdentifier expression;
    private boolean add;
    
    public ASTExpressionPlusPlus(List<AbstractSyntaxTree> tokens) {
        expression = (ASTExpressionIdentifier)tokens.get(0);
        Token operator = (Token)tokens.get(1);
        if(operator.getTokenType() == TokenType.PLUSPLUS) {
            add = true;
        } else if(operator.getTokenType() == TokenType.MINUSMINUS) {
            add = false;
        } else {
            throw new RuntimeException("Incorrect operator ++/--." +
                    "\tLine " + expression.getToken().getLineNumber());
        }
    }
    
    @Override
    public GObject run(Environment env, ASTFunction function) throws GScriptException {
        GObject value = expression.run(env, function);
        if(value.getType() != Type.INT && value.getType() != Type.FLOAT) {
            throw new GScriptException("Cannot ++ on a non-number." +
                    "\tLine " + expression.getToken().getLineNumber());
        }
        if(value.getType() == Type.INT) {
            BigInteger numValue = (BigInteger)value.getValue();
            numValue = numValue.add(BigInteger.valueOf(add ? 1 : -1));
            env.setValueForIdentifier(expression.getToken().getValue(), new GObject(numValue, Type.INT));
        } else if(value.getType() == Type.FLOAT) {
            BigDecimal numValue = (BigDecimal)value.getValue();
            numValue.add(BigDecimal.valueOf(add ? 1 : -1));
            env.setValueForIdentifier(expression.getToken().getValue(), new GObject(numValue, Type.FLOAT));
        }
        return value;
    }
}

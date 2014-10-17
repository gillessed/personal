package com.gillessed.gscript.ast;

import java.util.List;

public class ASTBlockForHeader extends AbstractSyntaxTree {

    private ASTStatement start;
    private ASTExpression condition;
    private ASTStatement loopFunction;
    public ASTBlockForHeader(List<AbstractSyntaxTree> tokens) {
        super(tokens.get(0).getLineNumber());
        AbstractSyntaxTree token;
        int index = 2; // for (
        token = tokens.get(index);
        if(token.getParseType().isSubtypeOf(new ParseType(ASTStatement.class))) {
            start = (ASTStatement)tokens.get(index);
            index++;
        }
        index++; // ;
        token = tokens.get(index);
        if(token.getParseType().isSubtypeOf(new ParseType(ASTExpression.class))) {
            condition = (ASTExpression)tokens.get(index);
            index++;
        }
        index++; // ;
        token = tokens.get(index);
        if(token.getParseType().isSubtypeOf(new ParseType(ASTStatement.class))) {
            loopFunction = (ASTStatement)tokens.get(index);
        }
    }

    public ASTStatement getStart() {
        return start;
    }

    public ASTExpression getCondition() {
        return condition;
    }

    public ASTStatement getLoopFunction() {
        return loopFunction;
    }
    
}

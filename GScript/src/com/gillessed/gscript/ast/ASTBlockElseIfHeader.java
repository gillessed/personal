package com.gillessed.gscript.ast;

import java.util.List;

public class ASTBlockElseIfHeader extends AbstractSyntaxTree {

    private ASTExpression condition;
    
    public ASTBlockElseIfHeader(List<? extends AbstractSyntaxTree> tokens) {
        super(tokens.get(0).getLineNumber());
        condition = (ASTExpression)tokens.get(3);
    }

    public ASTExpression getCondition() {
        return condition;
    }
}
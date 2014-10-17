package com.gillessed.gscript.ast;

import java.util.List;

public class ASTBlockIfHeader extends AbstractSyntaxTree {

    private ASTExpression condition;
    
    public ASTBlockIfHeader(List<? extends AbstractSyntaxTree> tokens) {
        super(tokens.get(0).getLineNumber());
        condition = (ASTExpression)tokens.get(2);
    }

    public ASTExpression getCondition() {
        return condition;
    }
}

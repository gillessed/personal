package com.gillessed.gscript.ast;

import java.util.ArrayList;
import java.util.List;

import com.gillessed.gscript.TokenType;

public class ASTBlockIf extends AbstractSyntaxTree {

    private ASTBlockIfHeader header;
    private List<ASTStatement> statements;
    
    public ASTBlockIf(List<? extends AbstractSyntaxTree> tokens) {
        super(tokens.get(0).getLineNumber());
        statements = new ArrayList<>();
        header = (ASTBlockIfHeader)tokens.get(0);
        int index = 2;
        AbstractSyntaxTree token = tokens.get(index);
        statements = new ArrayList<>();
        while(!token.getParseType().isSubtypeOf(TokenType.RSQUIG)) {
            statements.add((ASTStatement)tokens.get(index));
            index++;
            token = tokens.get(index);
        }
    }

    public ASTExpression getCondition() {
        return header.getCondition();
    }

    public List<ASTStatement> getStatements() {
        return statements;
    }
}

package com.gillessed.gscript.ast;

import java.util.ArrayList;
import java.util.List;

import com.gillessed.gscript.TokenType;

public class ASTBlockElse extends AbstractSyntaxTree {

    private List<ASTStatement> statements;
    
    public ASTBlockElse(List<? extends AbstractSyntaxTree> tokens) {
        super(tokens.get(0).getLineNumber());
        int index = 2;
        AbstractSyntaxTree token = tokens.get(index);
        statements = new ArrayList<>();
        while(!token.getParseType().isSubtypeOf(TokenType.RSQUIG)) {
            statements.add((ASTStatement)tokens.get(index));
            index++;
            token = tokens.get(index);
        }
    }

    public List<ASTStatement> getStatements() {
        return statements;
    }
}

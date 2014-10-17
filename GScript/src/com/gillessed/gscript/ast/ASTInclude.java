package com.gillessed.gscript.ast;

import java.util.List;

import com.gillessed.gscript.Token;

public class ASTInclude extends AbstractSyntaxTree {
    
    private final String includePath;
    
    public ASTInclude(List<AbstractSyntaxTree> tokens) {
        super(tokens.get(0).getLineNumber());
        includePath = ((Token)tokens.get(1)).getValue();
    }

    public String getIncludePath() {
        return includePath;
    }
}

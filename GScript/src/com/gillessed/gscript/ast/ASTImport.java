package com.gillessed.gscript.ast;

import java.util.List;

public class ASTImport extends AbstractSyntaxTree {

    public ASTImport(List<AbstractSyntaxTree> tokens) {
        super(tokens.get(0).getLineNumber());
    }
}

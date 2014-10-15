package com.gillessed.gscript.parser;

import java.util.List;

import com.gillessed.gscript.ast.AbstractSyntaxTree;

public class ParseNodeStarAccept extends ParseNode {

    @Override
    public ParseResultType parse(List<AbstractSyntaxTree> abstractSyntaxTree, int index) {
        return new ParseResultType(null, index - 1);
    }

}

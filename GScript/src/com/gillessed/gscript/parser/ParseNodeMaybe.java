package com.gillessed.gscript.parser;

import java.util.List;

import com.gillessed.gscript.ast.AbstractSyntaxTree;
import com.gillessed.gscript.ast.ParseType;

public class ParseNodeMaybe extends ParseNode {
    
    private List<ParseType> types;
    private boolean frontSkip;
    private boolean backSkip;

    public ParseNodeMaybe(List<ParseType> types, boolean frontSkip, boolean backSkip) {
        this.types = types;
        this.frontSkip = frontSkip;
        this.backSkip = backSkip;
        if(types == null || types.isEmpty()) {
            throw new RuntimeException("SingleAcceptParseNode cannot have a null type.");
        }
    }

    @Override
    public ParseResultType parse(List<AbstractSyntaxTree> abstractSyntaxTree, int index) {
        boolean match = false;
        if(index < abstractSyntaxTree.size()) {
            if(children.isEmpty()) {
                throw new RuntimeException("Maybe nodes must have a child otherwise, they can make mistakes in parsing.");
            }
            match = false;
            for(ParseType type : types) {
                if(abstractSyntaxTree.get(index).getParseType().isSubtypeOf(type)) {
                    match = true;
                    break;
                }
            }
        }
        ParseResultType result = null;
        for(ParseNode child : children) {
            result = child.parse(abstractSyntaxTree, index + (match ? 1 : 0));
            if(result != null) {
                if(frontSkip && result.startIndex == -1) {
                    result.startIndex = index + 1;
                }
                if(backSkip) {
                    result.index = index;
                }
                return result;
            }
        }
        return null;
    }
    
}

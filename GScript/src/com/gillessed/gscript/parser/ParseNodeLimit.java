package com.gillessed.gscript.parser;

import java.util.List;

import com.gillessed.gscript.ast.AbstractSyntaxTree;

public class ParseNodeLimit extends ParseNode {
    
    private boolean front;

    public ParseNodeLimit(boolean front) {
        this.front = front;
    }

    @Override
    public ParseResultType parse(List<AbstractSyntaxTree> abstractSyntaxTree, int index) {
        if(front && index == 0) {
            ParseResultType result = null;
            for(ParseNode child : children) {
                result = child.parse(abstractSyntaxTree, index);
                if(result != null) {
                    return result;
                }
            }
        } else if(!front && index == abstractSyntaxTree.size()) {
            ParseResultType result = null;
            for(ParseNode child : children) {
                result = child.parse(abstractSyntaxTree, index);
                if(result != null) {
                    return result;
                }
            }
        }
        return null;
    }

}

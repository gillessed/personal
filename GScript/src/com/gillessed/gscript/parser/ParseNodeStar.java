package com.gillessed.gscript.parser;

import java.util.List;

import com.gillessed.gscript.ast.AbstractSyntaxTree;

public class ParseNodeStar extends ParseNode {
    
    private List<ParseNode> cycleNodes;

    public ParseNodeStar(List<ParseNode> cycleNodes) {
        this.cycleNodes = cycleNodes;
        for(ParseNode node : cycleNodes) {
            node.addChild(new ParseNodeStarAccept());
        }
    }

    @Override
    public ParseResultType parse(List<AbstractSyntaxTree> abstractSyntaxTree, int index) {
        int starIndex = index;
        boolean broken = false;
        boolean nulled = false;
        while(!nulled) {
            for(int i = 0; i < cycleNodes.size(); i++) {
                ParseResultType result = cycleNodes.get(i).parse(abstractSyntaxTree, starIndex);
                if(result == null) {
                    if(i != 0) {
                        broken = true;
                    }
                    nulled = true;
                    break;
                } else {
                    starIndex = result.index + 1;
                }
            }
        }
        
        if(!broken) {
            ParseResultType result = null;
            for(ParseNode child : children) {
                result = child.parse(abstractSyntaxTree, starIndex);
                if(result != null) {
                    return result;
                }
            }
            return null;
        } else {
            return null;
        }
    }
}

package com.gillessed.gscript.parser;

import java.util.List;

import com.gillessed.gscript.ast.AbstractSyntaxTree;

public class ParseTreeList {
    private List<ParseNodeRoot> parseTrees;
    
    public ParseTreeList(List<ParseNodeRoot> parseTrees) {
        this.parseTrees = parseTrees;
    }
    
    public ParseResultType parse(List<AbstractSyntaxTree> abstractSyntaxTree) {
        for(ParseNodeRoot parseTree : parseTrees) {
            for(int i = 0; i < abstractSyntaxTree.size(); i++) {
                ParseResultType result = parseTree.parse(abstractSyntaxTree, i);
                if(result != null) {
                    if(result.startIndex == -1) {
                        result.startIndex = i;
                    }
                    return result;
                }
            }
        }
        return null;
    }
}

package com.gillessed.gscript.parser;

import java.util.List;

import com.gillessed.gscript.ast.AbstractSyntaxTree;
import com.gillessed.gscript.ast.ParseType;

public class ParseNodeSingleRead extends ParseNode {
	
	private List<ParseType> types;
    private boolean frontSkip;
    private boolean backSkip;

	public ParseNodeSingleRead(List<ParseType> types, boolean frontSkip, boolean backSkip) {
		this.types = types;
        this.frontSkip = frontSkip;
        this.backSkip = backSkip;
		if(types == null || types.isEmpty()) {
			throw new RuntimeException("SingleAcceptParseNode cannot have a null type.");
		}
	}

	@Override
	public ParseResultType parse(List<AbstractSyntaxTree> abstractSyntaxTree, int index) {
	    if(index >= abstractSyntaxTree.size()) {
	        if(!backSkip) { 
	            return null;
	        } else {
	            ParseResultType result = null;
	            for(ParseNode child : children) {
	                result = child.parse(abstractSyntaxTree, index + 1);
	                if(result != null) {
	                    if(frontSkip && result.startIndex == -1) {
	                        result.startIndex = index + 1;
	                    }
	                    if(backSkip) {
	                        result.index = index - 1;
	                    }
	                    return result;
	                }
	            }
	            return null;
	        }
	    }
	    boolean match = false;
	    for(ParseType type : types) {
            if(abstractSyntaxTree.get(index).getParseType().isSubtypeOf(type)) {
	            match = true;
	            break;
	        }
	    }
		if(match) {
			ParseResultType result = null;
			for(ParseNode child : children) {
				result = child.parse(abstractSyntaxTree, index + 1);
				if(result != null) {
                    if(frontSkip && result.startIndex == -1) {
                        result.startIndex = index + 1;
                    }
                    if(backSkip) {
                        result.index = index - 1;
                    }
					return result;
				}
			}
			return null;
		} else {
			return null;
		}
	}
	
}

package com.gillessed.gscript.parser;

import java.util.List;

import com.gillessed.gscript.ast.AbstractSyntaxTree;
import com.gillessed.gscript.ast.ParseType;

public class ParseNodeSingleRead extends ParseNode {
	
	private ParseType type;

	public ParseNodeSingleRead(ParseType type) {
		this.type = type;
		if(type == null) {
			throw new RuntimeException("SingleAcceptParseNode cannot have a null type.");
		}
	}

	@Override
	public ParseResultType parse(List<AbstractSyntaxTree> abstractSyntaxTree, int index) {
		if(type.equals(abstractSyntaxTree.get(index))) {
			ParseResultType result = null;
			for(ParseNode child : children) {
				result = child.parse(abstractSyntaxTree, index + 1);
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

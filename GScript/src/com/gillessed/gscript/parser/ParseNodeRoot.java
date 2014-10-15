package com.gillessed.gscript.parser;

import java.util.List;

import com.gillessed.gscript.ast.AbstractSyntaxTree;

public class ParseNodeRoot extends ParseNode {
	@Override
	public ParseResultType parse(List<AbstractSyntaxTree> abstractSyntaxTree, int index) {
		for(ParseNode child : children) {
			ParseResultType result = child.parse(abstractSyntaxTree, index);
			if(result != null) {
				return result;
			}
		}
		return null;
	}
}

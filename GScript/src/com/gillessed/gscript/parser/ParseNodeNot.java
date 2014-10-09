package com.gillessed.gscript.parser;

import java.util.List;

import com.gillessed.gscript.ast.AbstractSyntaxTree;
import com.gillessed.gscript.ast.ParseType;

public class ParseNodeNot extends ParseNode {
	
	private List<ParseType> types;

	public ParseNodeNot(List<ParseType> types) {
		this.types = types;
	}

	@Override
	public ParseResultType parse(List<AbstractSyntaxTree> abstractSyntaxTree, int index) {
		boolean found = false;
		for(ParseType type : types) {
			if(type.equals(abstractSyntaxTree.get(index))) {
				found = true;
				break;
			}
		}
		if(!found) {
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

package com.gillessed.gscript.parser;

import java.util.List;

import com.gillessed.gscript.ast.AbstractSyntaxTree;

public class ParseNodeAccept extends ParseNode {

	private Class<? extends AbstractSyntaxTree> resultType;

	public ParseNodeAccept(Class<? extends AbstractSyntaxTree> resultType) {
		this.resultType = resultType;
	}
	
	@Override
	public ParseResultType parse(List<AbstractSyntaxTree> abstractSyntaxTree, int index) {
		ParseResultType result = new ParseResultType(resultType, index);
		return result;
	}

}

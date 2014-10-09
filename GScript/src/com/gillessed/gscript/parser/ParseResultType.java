package com.gillessed.gscript.parser;

import com.gillessed.gscript.ast.AbstractSyntaxTree;

public class ParseResultType {
	public Class<? extends AbstractSyntaxTree> type;
	public int index;
	
	public ParseResultType(Class<? extends AbstractSyntaxTree> type, int index) {
		this.type = type;
		this.index = index;
	}
}

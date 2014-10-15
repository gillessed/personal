package com.gillessed.gscript.ast;


public abstract class AbstractSyntaxTree {
	public ParseType getParseType() {
		return new ParseType(getClass());
	}
}
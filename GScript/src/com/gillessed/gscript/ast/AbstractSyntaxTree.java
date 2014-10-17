package com.gillessed.gscript.ast;


public abstract class AbstractSyntaxTree {
    private int lineNumber;

    public AbstractSyntaxTree(int lineNumber) {
        this.lineNumber = lineNumber;
    }
    
    public int getLineNumber() {
        return lineNumber;
    }
    
	public ParseType getParseType() {
		return new ParseType(getClass());
	}
}
package com.gillessed.gscript;

import com.gillessed.gscript.ast.AbstractSyntaxTree;
import com.gillessed.gscript.ast.ParseType;

public class Token extends AbstractSyntaxTree {
	private final TokenType tokenType;
	private final String value;
	private final int lineNumber;
	
	public Token(TokenType tokenType, String value, int lineNumber) {
		this.tokenType = tokenType;
		this.value = value;
		this.lineNumber = lineNumber;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public String getValue() {
		return value;
	}

	public int getLineNumber() {
		return lineNumber;
	}
	
	@Override
	public ParseType getParseType() {
		return new ParseType(Token.class, tokenType);
	}

	@Override
	public String toString() {
		return tokenType.toString() + " (" + value + ")";
	}
}
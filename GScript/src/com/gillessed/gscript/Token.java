package com.gillessed.gscript;

import com.gillessed.gscript.ast.AbstractSyntaxTree;
import com.gillessed.gscript.ast.ParseType;

public class Token extends AbstractSyntaxTree {
	private final TokenType tokenType;
	private final String value;
	
	public Token(TokenType tokenType, String value, int lineNumber) {
	    super(lineNumber);
		this.tokenType = tokenType;
		this.value = value;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public String getValue() {
		return value;
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
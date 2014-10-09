package com.gillessed.gscript.ast;

import com.gillessed.gscript.Token;
import com.gillessed.gscript.TokenType;

/**
 * This class encapsulates the abstract syntax tree types. Since a token
 * is a type, but the specific token type must be differentiated, this
 * will compare types, and if need be, will also compare the token type.
 */
public class ParseType {
	private final Class<? extends AbstractSyntaxTree> type;
	private final TokenType tokenType;
	
	public ParseType(Class<? extends AbstractSyntaxTree> type, TokenType tokenType) {
		this.type = type;
		this.tokenType = tokenType;
		if(type == Token.class && tokenType == null) {
			throw new RuntimeException("One can not have a non-typed token.");
		}
		if(type != Token.class && tokenType != null) {
			throw new RuntimeException("One should not have a non=token type with a specific tokentype.");
		}
	}
	
	public ParseType(Class<? extends AbstractSyntaxTree> type) {
		this(type, null);
	}
	
	public Class<? extends AbstractSyntaxTree> getType() {
		return type;
	}
	
	public TokenType getTokenType() {
		return tokenType;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ParseType)) {
			return false;
		}
		ParseType t = (ParseType)obj;
		return (type == t.getType()) && (tokenType == t.getTokenType());
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result += type.hashCode() * 31;
		if(tokenType != null) {
			result += tokenType.hashCode();
		}
		return result;
	}
	
	@Override
	public String toString() {
		if(tokenType == null) {
			return type.getSimpleName();
		} else {
			return tokenType.toString();
		}
	}
}

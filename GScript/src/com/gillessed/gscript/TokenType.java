package com.gillessed.gscript;

public enum TokenType {
	IDENTIFIER,
	INT,	
	FLOAT,
	DEFINE,		//=
	EQUAL,		//==
	NEQUAL,		//!=
	NOT,		//!
	GT,			//>
	GTE,		//>=
	LT,			//<
	LTE,		//<=
	PLUS,		//+
	PLUSPLUS,	//++
	MINUS,		//-
	MINUSMINUS, //--
	TIMES,		//*
	DIVIDE,		///
	MOD,		//%
	EXP,		//**
	LOG,		////
	OR,			//||
	AND,		//&&
	XOR,		//^^
	LPAREN,		//(
	RPAREN,		//)
	LSQUARE,	//[
	RSQUARE,	//]
	LSQUIG,		//{
	RSQUIG,		//}
	DOT,		//.
	COMMA,		//,
	COLON,		//:
	SEMICOLON,	//;
	QMARK,		//?
	STRING,
	CHAR,
	TYPE,
	FUNCTION,
	RETURN,
	IF,
	ELSE,
	FOR,
	WHILE,
	DO,
	CLASS,
	AT,
	AS,
	LAM,
	IMPORT,
	OPERATOR,
	SWTICH,
	CASE,
	DEFAULT,
	BREAK,
	CONTINUE,
	BOOLEAN,
	NEW,
	NULL,
	INCLUDE,
	PAD;

    public static TokenType fromString(String string) {
        for(TokenType type : TokenType.values()) {
            if(type.toString().equals(string)) {
                return type;
            }
        }
        return null;
    }
}
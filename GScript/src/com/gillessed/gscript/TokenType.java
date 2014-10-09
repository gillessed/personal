package com.gillessed.gscript;

public enum TokenType {
	IDENTIFIER,
	LONG,	
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
	BINOR,		//|
	BINAND,		//&
	BINXOR,		//^
	SHIFTLEFT,	//<<
	SHIFTRIGHT,	//>>
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
	LAM,
	IMPORT,
	OPERATOR,
	SWTICH,
	CASE,
	DEFAULT,
	BREAK,
	CONTINUE,
	BOOLEAN,
	NEW
}
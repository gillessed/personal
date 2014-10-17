package com.gillessed.gscript;

import java.util.ArrayList;
import java.util.List;

public class GScriptTokenizer {
	private final List<String> codeLines;
	private List<Token> tokens = new ArrayList<>();
	private int column = 0;
	private StringBuffer tokenValue = new StringBuffer();
	private TokenState state = TokenState.START;
	private int lineNumber;
	
	public GScriptTokenizer(List<String> codeLines) {
		this.codeLines = codeLines;
	}
	
	public List<Token> tokenize() throws ParseException {
		for(lineNumber = 1; lineNumber <= codeLines.size(); lineNumber++) {
			String code = codeLines.get(lineNumber - 1);
			for(column = 0; column < code.length(); column++) {
				char ch = code.charAt(column);
				read(ch);
			}
		}
		read('\0');
		return tokens;
	}
	
	private void read(char ch) throws ParseException {
		switch(state) {
		case START:
			if(isIDStart(ch)) {
				state = TokenState.WORD;
				tokenValue.append(ch);
			} else if(Character.isDigit(ch)) {
				state = TokenState.NUMBER;
				tokenValue.append(ch);
			} else if(Character.isWhitespace(ch) || ch == '\0') {
			} else {
				tokenValue.append(ch);
				switch(ch) {
				case '#': state = TokenState.COMMENT; break;
				case '+': state = TokenState.PLUS; break;
				case '-': state = TokenState.MINUS; break;
				case '*': state = TokenState.TIMES; break;
				case '/': state = TokenState.DIV; break;
				case '!': state = TokenState.NOT; break;
				case '=': state = TokenState.EQUALS; break;
				case '>': state = TokenState.GT; break;
				case '<': state = TokenState.LT; break;
				case '"': state = TokenState.STRING; break;
				case '\'': state = TokenState.CHAR; break;
				case '%':
					finish(new Token(TokenType.MOD, tokenValue.toString(), lineNumber)); break;
				case '(':
					finish(new Token(TokenType.LPAREN, tokenValue.toString(), lineNumber)); break;
				case ')': 
					finish(new Token(TokenType.RPAREN, tokenValue.toString(), lineNumber)); break;
				case '[':
					finish(new Token(TokenType.LSQUARE, tokenValue.toString(), lineNumber)); break;
				case ']':
					finish(new Token(TokenType.RSQUARE, tokenValue.toString(), lineNumber)); break;
				case '{':
					finish(new Token(TokenType.LSQUIG, tokenValue.toString(), lineNumber)); break;
				case '}':
					finish(new Token(TokenType.RSQUIG, tokenValue.toString(), lineNumber)); break;
				case '.':
					finish(new Token(TokenType.DOT, tokenValue.toString(), lineNumber)); break;
				case ',':
					finish(new Token(TokenType.COMMA, tokenValue.toString(), lineNumber)); break;
				case ':':
					finish(new Token(TokenType.COLON, tokenValue.toString(), lineNumber)); break;
				case ';':
					finish(new Token(TokenType.SEMICOLON, tokenValue.toString(), lineNumber)); break;
				case '?':
					finish(new Token(TokenType.QMARK, tokenValue.toString(), lineNumber)); break;
				default:
					throw new ParseException("Error parsing token \"" + tokenValue +
							"\" at line " + lineNumber);
				}
			}
			break;
		case WORD:
			if(isID(ch)) {
				tokenValue.append(ch);
			} else {
				if(!Character.isWhitespace(ch)) {
					column--;
				}
				TokenType type = TokenType.IDENTIFIER;
				switch(tokenValue.toString()) {
				case "type": type = TokenType.TYPE; break;
				case "function": type = TokenType.FUNCTION; break;
				case "return": type = TokenType.RETURN; break;
				case "if": type = TokenType.IF; break;
				case "else": type = TokenType.ELSE; break;
				case "for": type = TokenType.FOR; break;
				case "while": type = TokenType.WHILE; break;
				case "do": type = TokenType.DO; break;
				case "class": type = TokenType.CLASS; break;
				case "at": type = TokenType.AT; break;
                case "as": type = TokenType.AS; break;
				case "lam": type = TokenType.LAM; break;
				case "import": type = TokenType.IMPORT; break;
				case "operator": type = TokenType.OPERATOR; break;
				case "switch": type = TokenType.SWTICH; break;
				case "case": type = TokenType.CASE; break;
				case "default": type = TokenType.DEFAULT; break;
				case "break": type = TokenType.BREAK; break;
				case "continue": type = TokenType.CONTINUE; break;
				case "true": type = TokenType.BOOLEAN; break;
				case "false": type = TokenType.BOOLEAN; break;
				case "new": type = TokenType.NEW; break;
                case "null": type = TokenType.NULL; break;
                case "include": type = TokenType.INCLUDE; break;
                case "run": type = TokenType.NULL; break;
				}
				finish(new Token(type, tokenValue.toString(), lineNumber));
			}
			break;
		case COMMENT:
			if(ch == '#') {
				finish(null);
			}
			break;
		case NUMBER:
			if(Character.isDigit(ch)) {
				tokenValue.append(ch);
			} else if(Character.isWhitespace(ch)) {
				finish(new Token(TokenType.INT, tokenValue.toString(), lineNumber));
			} else if(ch == '.') {
				tokenValue.append(ch);
				state = TokenState.DOUBLE;
			} else if(ch == 'e' || ch == 'E') {
				tokenValue.append(ch);
				state = TokenState.FLOAT_E1;
			} else {
				column--;
				finish(new Token(TokenType.INT, tokenValue.toString(), lineNumber));
			}
			break;
		case DOUBLE:
			if(Character.isDigit(ch)) {
				tokenValue.append(ch);
			} else if(Character.isWhitespace(ch)) {
				finish(new Token(TokenType.FLOAT, tokenValue.toString(), lineNumber));
			} else if(ch == 'e' || ch == 'E') {
				tokenValue.append(ch);
				state = TokenState.FLOAT_E1;
			} else {
				column--;
				finish(new Token(TokenType.FLOAT, tokenValue.toString(), lineNumber));
			}
			break;
		case FLOAT_E1:
			if(Character.isDigit(ch) || ch == '-') {
				tokenValue.append(ch);
				state = TokenState.FLOAT_E2;
			} else if(Character.isWhitespace(ch)) {
				throw new ParseException("Incomplete float: \'" + tokenValue + "\" on line " + lineNumber);
			}
			break;
		case FLOAT_E2:
			if(Character.isDigit(ch)) {
				tokenValue.append(ch);
			} else if(Character.isWhitespace(ch)) {
				finish(new Token(TokenType.FLOAT, tokenValue.toString(), lineNumber));
			} else {
				column--;
				finish(new Token(TokenType.FLOAT, tokenValue.toString(), lineNumber));
			}
			break;
		case EQUALS:
			if(Character.isWhitespace(ch)) {
				finish(new Token(TokenType.DEFINE, tokenValue.toString(), lineNumber));
			} else if(ch == '=') {
				tokenValue.append(ch);
				finish(new Token(TokenType.EQUAL, tokenValue.toString(), lineNumber));
			} else {
				column--;
				finish(new Token(TokenType.DEFINE, tokenValue.toString(), lineNumber));
			}
			break;
		case PLUS:
			if(Character.isWhitespace(ch)) {
				finish(new Token(TokenType.PLUS, tokenValue.toString(), lineNumber));
			} else if(ch == '+') {
				tokenValue.append(ch);
				finish(new Token(TokenType.PLUSPLUS, tokenValue.toString(), lineNumber));
			} else {
				column--;
				finish(new Token(TokenType.PLUS, tokenValue.toString(), lineNumber));
			}
			break;
		case MINUS:
			if(Character.isWhitespace(ch)) {
				finish(new Token(TokenType.MINUS, tokenValue.toString(), lineNumber));
			} else if(ch == '-') {
				tokenValue.append(ch);
				finish(new Token(TokenType.MINUSMINUS, tokenValue.toString(), lineNumber));
			} else {
				column--;
				finish(new Token(TokenType.MINUS, tokenValue.toString(), lineNumber));
			}
			break;
		case TIMES:
			if(Character.isWhitespace(ch)) {
				finish(new Token(TokenType.TIMES, tokenValue.toString(), lineNumber));
			} else if(ch == '*') {
				tokenValue.append(ch);
				finish(new Token(TokenType.EXP, tokenValue.toString(), lineNumber));
			} else {
				column--;
				finish(new Token(TokenType.TIMES, tokenValue.toString(), lineNumber));
			}
			break;
		case DIV:
			if(Character.isWhitespace(ch)) {
				finish(new Token(TokenType.DIVIDE, tokenValue.toString(), lineNumber));
			} else if(ch == '/') {
				tokenValue.append(ch);
				finish(new Token(TokenType.LOG, tokenValue.toString(), lineNumber));
			} else {
				column--;
				finish(new Token(TokenType.DIVIDE, tokenValue.toString(), lineNumber));
			}
			break;
		case NOT:
			if(Character.isWhitespace(ch)) {
				finish(new Token(TokenType.NOT, tokenValue.toString(), lineNumber));
			} else if(ch == '=') {
				tokenValue.append(ch);
				finish(new Token(TokenType.NEQUAL, tokenValue.toString(), lineNumber));
			} else {
				column--;
				finish(new Token(TokenType.NOT, tokenValue.toString(), lineNumber));
			}
			break;
		case BINAND:
			if(ch == '&') {
				tokenValue.append(ch);
				finish(new Token(TokenType.AND, tokenValue.toString(), lineNumber));
			} else {
			    throw new ParseException("Invalid character \"" + tokenValue.toString() + "\" on line " + lineNumber);
			}
			break;
		case BINOR:
			if(ch == '|') {
				tokenValue.append(ch);
				finish(new Token(TokenType.OR, tokenValue.toString(), lineNumber));
			} else {
			    throw new ParseException("Invalid character \"" + tokenValue.toString() + "\" on line " + lineNumber);
			}
		break;
		case BINXOR:
			if(ch == '^') {
				tokenValue.append(ch);
				finish(new Token(TokenType.XOR, tokenValue.toString(), lineNumber));
			} else {
			    throw new ParseException("Invalid character \"" + tokenValue.toString() + "\" on line " + lineNumber);
			}
		break;
		case GT:
			if(Character.isWhitespace(ch)) {
				finish(new Token(TokenType.GT, tokenValue.toString(), lineNumber));
			} else if(ch == '=') {
				tokenValue.append(ch);
				finish(new Token(TokenType.GTE, tokenValue.toString(), lineNumber));
			} else {
				column--;
				finish(new Token(TokenType.GT, tokenValue.toString(), lineNumber));
			}
		break;
		case LT:
			if(Character.isWhitespace(ch)) {
				finish(new Token(TokenType.LT, tokenValue.toString(), lineNumber));
			} else if(ch == '=') {
				tokenValue.append(ch);
				finish(new Token(TokenType.LTE, tokenValue.toString(), lineNumber));
			}else {
				column--;
				finish(new Token(TokenType.LT, tokenValue.toString(), lineNumber));
			}
		break;
		case CHAR:
			tokenValue.append(ch);
			if(ch == '\\') {
				state = TokenState.CHAR_E;
			} else {
				state = TokenState.CHAR_F;
			}
		break;
		case CHAR_E:
			tokenValue.append(ch);
			state = TokenState.CHAR_F;
		break;
		case CHAR_F:
			if(ch == '\'') {
				tokenValue.append(ch);
				finish(new Token(TokenType.CHAR, tokenValue.toString(), lineNumber));
			} else {
				throw new ParseException("Invalid character \"" + tokenValue.toString() + "\" on line " + lineNumber);
			}
		break;
		case STRING:
			tokenValue.append(ch);
			if(ch == '\\') {
				state = TokenState.STRING_E;
			} else if(ch == '"') {
				finish(new Token(TokenType.STRING, tokenValue.toString(), lineNumber));
			}
		break;
		case STRING_E:
			tokenValue.append(ch);
			if(ch == '"') {
				throw new ParseException("Invalid character \"" + tokenValue.toString() + "\" on line " + lineNumber);
			} else {
				state = TokenState.STRING;
			}
		break;
		}
	}
	
	private void finish(Token token) {
		if(token != null) {
			tokens.add(token);
		}
		state = TokenState.START;
		tokenValue = new StringBuffer();
	}
	
	private boolean isIDStart(char ch) {
		return Character.isLetter(ch) || ch == '_';
	}
	
	private boolean isID(char ch) {
		return Character.isLetterOrDigit(ch) || ch == '_';
	}
}

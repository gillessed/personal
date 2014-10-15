package com.gillessed.gscript.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gillessed.gscript.Token;
import com.gillessed.gscript.TokenType;
import com.gillessed.gscript.ast.AbstractSyntaxTree;
import com.gillessed.gscript.ast.ParseType;

/**
 * This class reads and parses the grammar file into parse nodes
 * which will then be used to parse the script language itself.
 * 
 * @author gcole
 */
public class GrammarReader {
	private String grammarFile;

    public GrammarReader(String grammarFile) {
        this.grammarFile = grammarFile;
	}

	public ParseTreeList read() throws IOException {
	    BufferedReader rd = new BufferedReader(new FileReader(grammarFile));
	    List<ParseNodeRoot> parseNodeRoots = new ArrayList<>();
	    ParseNodeRoot currentRoot = new ParseNodeRoot();
	    String line;
	    int lineNumber = 1;
	    while((line = rd.readLine()) != null) {
	        line.trim();
	        if(line.isEmpty() || line.charAt(0) == '%') {
	            //No-op
	        } else if(line.charAt(0) == '$') {
	            parseNodeRoots.add(currentRoot);
	            currentRoot = new ParseNodeRoot();
	        } else {
	            List<ParseNode> nodes = parseLine(line, lineNumber);
	            ParseNode currentNode = nodes.get(0);
	            for(int i = 1; i < nodes.size(); i++) {
	                currentNode.addChild(nodes.get(i));
	                currentNode = nodes.get(i);
	            }
	            currentRoot.addChild(nodes.get(0));
	        }
	        lineNumber++;
	    }
	    rd.close();
		return new ParseTreeList(parseNodeRoots);
	}
	
	public List<ParseNode> parseLine(String line, int lineNumber) {
	    List<ParseNode> nodes = new ArrayList<>();
	    int index = 0;
	    int paren = 0;
	    int squig = 0;
	    String chunk = "";
	    while(index < line.length()) {
	        while(index < line.length() && (!(line.charAt(index) == '>') || paren != 0 || squig != 0)) {
	            char ch = line.charAt(index);
	            if(ch == '(') {
	                paren++;
	            } else if(ch == '{') {
	                squig++;
	            } else if(ch == ')') {
	                paren--;
	            } else if(ch == '}') {
	                squig--;
	            }
	            chunk += ch;
	            index++;
	        }
	        if(squig != 0 || paren != 0) {
	            throw new RuntimeException("Invalid grammar file: missing one of (){}");
	        }
	        chunk = chunk.trim();
	        boolean frontSkip = false;
            boolean backSkip = false;
	        if(chunk.charAt(0) == '[') {
	            frontSkip = true;
	            chunk = chunk.substring(1).trim();
	        } else if(chunk.charAt(0) == ']') {
	            backSkip = true;
                chunk = chunk.substring(1).trim();
            }
	        if(chunk.charAt(0) == '~') {
	            List<ParseType> types = parseTypeList(chunk.substring(1).trim(), lineNumber);
	            nodes.add(new ParseNodeSingleRead(types, frontSkip, backSkip));
	        } else if(chunk.charAt(0) == '!') {
                List<ParseType> types = parseTypeList(chunk.substring(1).trim(), lineNumber);
                nodes.add(new ParseNodeNot(types, frontSkip, backSkip));
            } else if(chunk.charAt(0) == '+') {
                List<ParseType> types = parseTypeList(chunk.substring(1).trim(), lineNumber);
                nodes.add(new ParseNodeMaybe(types, frontSkip, backSkip));
	        } else if(chunk.charAt(0) == '^') {
	            ParseType parseType = parseType(chunk.substring(1).trim(), lineNumber);
	            nodes.add(new ParseNodeAccept(parseType.getType()));
	        } else if(chunk.charAt(0) == '*') {
	            nodes.add(parseStar(chunk.substring(1).trim(), lineNumber));
	        } else if(chunk.charAt(0) == '@') {
                nodes.add(new ParseNodeLimit(true));
            } else if(chunk.charAt(0) == '&') {
                nodes.add(new ParseNodeLimit(false));
	        } else {
	            throw new RuntimeException("Unknown chunk: " + chunk + " at line " + lineNumber + ".");
	        }
	        index++;
	        chunk = "";
	    }
	    return nodes;
	}
	
	public ParseType parseType(String typeString, int lineNumber) {
	    if(typeString.charAt(0) == '#') {
	        return new ParseType(Token.class, TokenType.fromString(typeString.substring(1)));
	    } else {
	        String qualifier = "com.gillessed.gscript.ast.";
	        try {
                @SuppressWarnings("unchecked")
                Class<? extends AbstractSyntaxTree> clazz = 
                        (Class<? extends AbstractSyntaxTree>)(Class.forName(qualifier + typeString));
                return new ParseType(clazz);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Class not found: " + qualifier + typeString + " at line " + lineNumber + ".");
            }
	    }
	}
	
	public List<ParseType> parseTypeList(String chunk, int lineNumber) {
	    List<ParseType> types = new ArrayList<>();
	    if(chunk.charAt(0) == '{') {
	        if(chunk.charAt(chunk.length() - 1) != '}') {
	            throw new RuntimeException("Parse Error: } not found at line " + lineNumber + ".");
	        }
	        chunk = chunk.substring(1, chunk.length() - 1);
	        String[] split = chunk.split(",");
	        for(String str : split) {
	            ParseType type = parseType(str.trim(), lineNumber);
	            types.add(type);
	        }
	    } else {
	        types.add(parseType(chunk, lineNumber));
	    }
	    return types;
	}
	
	public ParseNodeStar parseStar(String chunk, int lineNumber) {
	    if(!(chunk.charAt(0) == '(') || !(chunk.charAt(chunk.length() - 1) == ')')) {
	        throw new RuntimeException("Error parsing *: missing ( or ) at line " + lineNumber + ".");
	    }
	    List<ParseNode> parseNode = parseLine(chunk.substring(1, chunk.length() - 1), lineNumber);
	    return new ParseNodeStar(parseNode);
	}
}

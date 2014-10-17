package com.gillessed.gscript;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.gillessed.gscript.ast.ASTProgram;
import com.gillessed.gscript.ast.AbstractSyntaxTree;
import com.gillessed.gscript.parser.GrammarReader;
import com.gillessed.gscript.parser.ParseResultType;
import com.gillessed.gscript.parser.ParseTreeList;

public class GScriptParser {
	private final List<Token> tokens;
	private final ParseTreeList parseTrees;
	
	public GScriptParser(List<Token> tokens) {
		this.tokens = tokens;
		GrammarReader reader = new GrammarReader("res" + File.separatorChar + "gscript.grammar");
		try {
            parseTrees = reader.read();
        } catch (IOException e) {
            throw new RuntimeException("Could not find grammar file.");
        }
	}
	
	public ASTProgram parse() throws ParseException {
		List<AbstractSyntaxTree> abstractSyntaxTree = new LinkedList<>();
		List<AbstractSyntaxTree> backupTree = new LinkedList<>();
		abstractSyntaxTree.addAll(tokens);
		abstractSyntaxTree.add(0, new Token(TokenType.PAD, "pad", 0));
        abstractSyntaxTree.add(0, new Token(TokenType.PAD, "pad", 0));
        abstractSyntaxTree.add(new Token(TokenType.PAD, "pad", 0));
        abstractSyntaxTree.add(new Token(TokenType.PAD, "pad", 0));
		while(abstractSyntaxTree.size() > 1) {
			ParseResultType resultType = parseTrees.parse(abstractSyntaxTree);
			if(resultType != null) {
				List<AbstractSyntaxTree> slice = new ArrayList<>();
				int value = resultType.index;
				int startIndex = resultType.startIndex;
				while(value >= startIndex) {
					slice.add(abstractSyntaxTree.remove(startIndex));
					value--;
				}
				try {
					AbstractSyntaxTree newElement = resultType.type.getConstructor(List.class).newInstance(slice);
					abstractSyntaxTree.add(startIndex, newElement);
				} catch (InstantiationException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException(e);
				} catch (SecurityException e) {
					throw new RuntimeException(e);
				}
			} else {
			    break;
			}
			if(!backupTree.isEmpty()) {
    			boolean match = true;
    			for(int i = 0; i < abstractSyntaxTree.size(); i++) {
    			    if(!abstractSyntaxTree.get(i).getParseType().equals(backupTree.get(i).getParseType())) {
    			        match = false;
    			        break;
    			    }
    			}
    			if(match) {
    			    break;
    			}}
			backupTree.clear();
			backupTree.addAll(abstractSyntaxTree);
		}
		if(!abstractSyntaxTree.get(0).getParseType().isSubtypeOf(ASTProgram.class)) {
            System.out.println("*** PARSER RESULTS ***");
		    for(AbstractSyntaxTree token : abstractSyntaxTree) {
		        System.out.println(token);
		    }
		    throw new ParseException("Could not parse program correctly. ");
		}
		return (ASTProgram)abstractSyntaxTree.get(0);
	}
}
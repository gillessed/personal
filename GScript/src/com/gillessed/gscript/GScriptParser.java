package com.gillessed.gscript;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.gillessed.gscript.ast.AbstractSyntaxTree;
import com.gillessed.gscript.parser.GrammarReader;
import com.gillessed.gscript.parser.ParseNodeRoot;
import com.gillessed.gscript.parser.ParseResultType;

public class GScriptParser {
	private final List<Token> tokens;
	private final ParseNodeRoot parseRules;
	
	public GScriptParser(List<Token> tokens) {
		this.tokens = tokens;
		GrammarReader reader = new GrammarReader("res" + File.separatorChar + "gscript.grammar");
		parseRules = reader.read();
	}
	
	public AbstractSyntaxTree parse() throws ParseException {
		LinkedList<AbstractSyntaxTree> abstractSyntaxTree = new LinkedList<>();
		abstractSyntaxTree.addAll(tokens);
		for(int co = 0; co < 10; co++) {
			for(int i = 0; i < abstractSyntaxTree.size(); i++) {
				ParseResultType resultType = parseRules.parse(abstractSyntaxTree, i);
				if(resultType != null) {
					List<AbstractSyntaxTree> slice = new ArrayList<>();
					int value = resultType.index;
					while(value >= i) {
						slice.add(abstractSyntaxTree.remove(i));
						value--;
					}
					try {
						AbstractSyntaxTree newElement = resultType.type.getConstructor(List.class).newInstance(slice);
						abstractSyntaxTree.add(i, newElement);
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
					break;
				}
			}
		}
		for(AbstractSyntaxTree ast : abstractSyntaxTree) {
			System.out.println(ast.getParseType());
		}
		return abstractSyntaxTree.get(0);
	}
}
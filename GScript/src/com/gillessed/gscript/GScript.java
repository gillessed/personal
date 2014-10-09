package com.gillessed.gscript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gillessed.gscript.ast.AbstractSyntaxTree;

public class GScript {
	
	/**
	 * Load a code from a file.
	 * @param filename
	 * @throws IOException if there is an error reading the file.
	 */
	public void loadFile(String filename) throws IOException, ParseException {
		loadFile(new File(filename));
	}
	
	/**
	 * Load a code from a file.
	 * @param file
	 * @throws IOException if there is an error reading the file.
	 */
	public void loadFile(File file) throws IOException, ParseException {
		BufferedReader rd = new BufferedReader(new FileReader(file));
		List<String> codeLines = new ArrayList<>();
		String line;
		while(!((line = rd.readLine()) == null)) {
			codeLines.add(line);
		}
		rd.close();
		load(codeLines);
	}
	
	/**
	 * Load code from a string in memeory.
	 * @param code
	 */
	public void loadString(String code) throws ParseException {
		List<String> codeLines = Arrays.asList(code.split("\n"));
		load(codeLines);
	}
	
	/**
	 * This function will actually load the code. It will tokenize
	 * and parse the code and then store it memory so it can be run.
	 * @param code
	 */
	private void load(List<String> codeLines) throws ParseException {
		GScriptTokenizer tokenizer = new GScriptTokenizer(codeLines);
		List<Token> tokens = tokenizer.tokenize();
		System.out.println("**** TOKENIZER RESULTS ****");
		for(Token token : tokens) {
			System.out.println(token.getTokenType().toString() + "(" + token.getValue() + ")");
		}
		System.out.println();
		GScriptParser parser = new GScriptParser(tokens);
		AbstractSyntaxTree ast = parser.parse();
	}
}

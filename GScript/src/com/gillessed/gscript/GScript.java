package com.gillessed.gscript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gillessed.gscript.ast.ASTProgram;

public class GScript {
    
    private ASTProgram program;
	
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
		GScriptParser parser = new GScriptParser(tokens);
		program = parser.parse();
	}
    
	/**
	 * Overload for {@code run(List<Object> arguments> arguments)}.
	 */
    public Object run(Object... arguments) throws GScriptException {
        List<Object> argumentList = new ArrayList<>();
        for(Object obj : arguments) {
            argumentList.add(obj);
        }
        return run(argumentList);
    }
	
    /**
     * 
     * @param arguments
     * @return
     * @throws GScriptException
     */
	public Object run(List<Object> arguments) throws GScriptException {
	    GObject result = program.run(arguments);
	    return GObjectConverter.convertFromGObject(result);
	}
}

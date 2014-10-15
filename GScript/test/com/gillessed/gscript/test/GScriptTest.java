package com.gillessed.gscript.test;

import java.io.File;
import java.io.IOException;

import com.gillessed.gscript.GScript;
import com.gillessed.gscript.GScriptException;
import com.gillessed.gscript.ParseException;

public class GScriptTest {
	public static void main(String args[]) throws ParseException, IOException {
		GScript gscript = new GScript();
		gscript.loadFile("testfiles" + File.separator + "test1.gs");
		System.out.println("Running...");
		try {
            Object result = gscript.run();
            System.out.println(result);
        } catch (GScriptException e) {
            System.err.println(e);
            System.exit(0);
        }
	}
}

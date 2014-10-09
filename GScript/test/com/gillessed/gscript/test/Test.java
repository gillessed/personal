package com.gillessed.gscript.test;

import java.io.IOException;

import com.gillessed.gscript.GScript;
import com.gillessed.gscript.ParseException;

public class Test {
	public static void main(String args[]) throws ParseException, IOException {
		GScript gscript = new GScript();
		gscript.loadFile("testfiles\\test1.gs");
	}
}

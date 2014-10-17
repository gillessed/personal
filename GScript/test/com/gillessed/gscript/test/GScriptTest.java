package com.gillessed.gscript.test;

import java.io.File;
import java.io.IOException;

import com.gillessed.gscript.GScript;
import com.gillessed.gscript.GScriptException;
import com.gillessed.gscript.ParseException;

public class GScriptTest {
	public static void main(String args[]) throws ParseException, IOException {
	    long test = 15485863;
	    
		GScript gscript = new GScript();
		gscript.loadFile("testfiles" + File.separator + "isprime.gs");
		long time1 = System.currentTimeMillis();
		try {
            Object result = gscript.run(test);
            System.out.println(result);
        } catch (GScriptException e) {
            System.err.println(e);
            System.exit(0);
        }
		
		long newtime2 = System.currentTimeMillis();
		double timeDiff = (newtime2 - time1) / 1000.0;
		System.out.println("Took: " + timeDiff + " seconds.");

        time1 = System.currentTimeMillis();
        boolean boolResult = isPrime(test);
        System.out.println(boolResult );
        newtime2 = System.currentTimeMillis();
        double timeDiff2 = (newtime2 - time1) / 1000.0;
        System.out.println("Took: " + timeDiff2 + " seconds.");
        
        System.out.println("Factor: " + timeDiff / timeDiff2);
	}
	
	private static boolean isPrime(long n) {
	    if(n <= 1) {
	        return false;
	    }
	    if(n == 2) {
	        return true;
	    }
	    boolean division = false;
	    for(long i = 2; i <= (n + 1) / 2; i++) {
	        if(n % i == 0) {
	            division = true;
	            i = n;
	        }
	    }
	    return !division;
	}
}

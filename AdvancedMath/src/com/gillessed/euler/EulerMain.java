package com.gillessed.euler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.gillessed.euler.utils.Timer;



public class EulerMain {
	public static void main(String args[]) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		int problemNum = 0;
		if(args.length == 0) {
			BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Run problem num: ");
			problemNum = Integer.parseInt(rd.readLine());
		} else {
			problemNum = Integer.parseInt(args[0]);
		}
		if(problemNum != 0) {
			Problem<?> prob = (Problem<?>) Class.forName("euler.Problem" + Integer.toString(problemNum)).newInstance();
			Timer.start();
			System.out.println("EVALUATE --- Value for problem " + problemNum + ": " + prob.evaluate());
			Timer.printResult();
		} else {
			for(int i = 1; i < 355; i++) {
				boolean classExists = true;
				try {
					Class.forName("euler.Problem" + Integer.toString(i));
				} catch(ClassNotFoundException e) {
					classExists = false;
				}
				if(classExists) {
					Problem<?> prob = (Problem<?>) Class.forName("euler.Problem" + Integer.toString(i)).newInstance();
					Timer.start();
					System.out.println("EVALUATE --- Value for problem " + i + ": " + prob.evaluate());
					Timer.printResult();
				}
			}
		}
	}
}

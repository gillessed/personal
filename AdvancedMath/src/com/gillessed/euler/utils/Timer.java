package com.gillessed.euler.utils;

public class Timer {
	private static long time;
	public static void start() {
		time = System.currentTimeMillis();
	}
	public static double result() {
		long lengthOfTime = System.currentTimeMillis() - time;
		return (double)lengthOfTime/1000d;
	}
	public static void printResult() {
		System.out.println("time: " + Timer.result() + "s");
	}
}

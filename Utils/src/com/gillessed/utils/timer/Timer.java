package com.gillessed.utils.timer;

/**
 * This class is a really simple timer.
 * 
 * @author gcole
 */
public class Timer {
	long startTime;
	
	public void start() {
		startTime = System.currentTimeMillis();
	}
	
	public long stop() {
		return System.currentTimeMillis() - startTime;
	}
	
	public double stopSeconds() {
		long millis = System.currentTimeMillis() - startTime;
		return ((double)millis) / 1000;
	}
}

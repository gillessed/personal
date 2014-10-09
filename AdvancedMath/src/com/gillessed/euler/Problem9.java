package com.gillessed.euler;


public class Problem9 implements Problem<Long> {
	public Long evaluate() {
		long sum = 1000;
		for(long a = 1; a < sum; a++) {
			for(long b = 1; b < sum; b++) {
				long c = 1000 - a - b;
				if(a * a + b * b == c * c) {
					return (a * b * c);
				}
			}
		}
		return -1l;
	}
}

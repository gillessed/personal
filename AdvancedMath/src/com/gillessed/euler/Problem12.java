package com.gillessed.euler;

import com.gillessed.euler.utils.Utils;


public class Problem12 implements Problem<Long> {
	public Long evaluate() {
		long aim = 500;
		long n = 2;
		long divisors = Utils.divisorCount(Utils.triangleNumber(n));
		while(divisors < aim) {
			n++;
			divisors = Utils.divisorCount(Utils.triangleNumber(n));
		}
		return Utils.triangleNumber(n);
	}
}

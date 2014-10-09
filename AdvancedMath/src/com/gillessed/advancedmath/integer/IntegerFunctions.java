package com.gillessed.advancedmath.integer;

public class IntegerFunctions {
	public static long gcd(long a, long b) {
		if(a == 0 || b == 0) return 0;
		if(a == 1 || b == 1) return 1;
		if(a == b) return a;
		long c, d, e;
		if (Math.abs(a) > Math.abs(b)) {
			c = Math.abs(a); d = Math.abs(b);
		} else {
			c = Math.abs(b); d = Math.abs(a);
		}
		while(d > 0) {
			e = c % d;
			c = d;
			d = e;
		}
		return c;
	}
}

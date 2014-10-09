package com.gillessed.euler;

import com.gillessed.euler.utils.Utils;

public class Problem7 implements Problem<Long> {
	public Long evaluate() {
		return Utils.nthPrime(10001);
	}
}

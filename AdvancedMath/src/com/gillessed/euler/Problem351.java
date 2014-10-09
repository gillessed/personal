package com.gillessed.euler;

import com.gillessed.euler.utils.Utils;

public class Problem351 implements Problem<Long> {

	@Override
	public Long evaluate() {

		long limit = 100000000;
		long sum = limit * (limit + 1) / 2 - 1;

		Utils.initTotientSieve((int)limit);

		long totientSummatory = 0;
		for(long i = 2; i <= limit; i++) {
			totientSummatory += Utils.totient(i);
		}

		sum -= totientSummatory;
		return sum * 6;
	}
}

package com.gillessed.euler;

import java.util.HashMap;
import java.util.Map;



public class Problem14 implements Problem<Long> {
	public Long evaluate() {
		long aim = 1000000;
		Map<Long, Long> collatzLength = new HashMap<Long, Long>();
		collatzLength.put(1l, 1l);
		for(long n = 2; n <= aim; n++) {
			long result = next(n);
			long length = 1;
			while(!collatzLength.containsKey(result)) {
				result = next(result);
				length++;
			}
			collatzLength.put(n, length + collatzLength.get(result));
		}
		long maxValue = 0;
		for(Map.Entry<Long, Long> l : collatzLength.entrySet()) {
			if(l.getValue() > maxValue) {
				maxValue = l.getValue();
			}
		}
		return maxValue;
	}
	public static long next(long l) {
		if(l % 2 == 0) {
			return l / 2;
		} else {
			return 3 * l + 1;
		}
	}
}

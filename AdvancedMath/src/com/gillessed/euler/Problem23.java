package com.gillessed.euler;

import java.util.ArrayList;
import java.util.List;

import com.gillessed.euler.utils.Utils;




public class Problem23 implements Problem<Long> {
	public Long evaluate() {
		List<Long> abundantNumbers = new ArrayList<Long>();
		for(long i = 2; i <= 28123; i++) {
			List<Long> divisors = Utils.properDivisors(i);
			long divisorSum = Utils.listSum(divisors);
			if(divisorSum > i) {
				abundantNumbers.add(i);
			}
		}
		List<Long> positiveIntegers = new ArrayList<Long>();
		for(long i = 1; i <= 28123; i++) {
			positiveIntegers.add(i);
		}
		for(int i = 0; i < abundantNumbers.size(); i++) {
			for(int j = 0; j < abundantNumbers.size(); j++) {
				positiveIntegers.remove(abundantNumbers.get(i) + abundantNumbers.get(j));
			}
		}
		return Utils.listSum(positiveIntegers);
	}
}

package com.gillessed.euler;

import java.util.ArrayList;
import java.util.List;

import com.gillessed.euler.utils.Utils;



public class Problem10 implements Problem<Long> {
	public Long evaluate() {
		long threshhold = 2000000;
		long prime = Utils.nthPrime(1);
		List<Long> primes = new ArrayList<Long>();
		while(prime < threshhold) {
			primes.add(prime);
			prime = Utils.nextPrime(prime + 1);
		}
		long sum = 0;
		for(Long l : primes) sum += l;
		return sum;
	}
}

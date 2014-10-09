package com.gillessed.euler;

import com.gillessed.euler.utils.Utils;



public class Problem17 implements Problem<Long> {
	public Long evaluate() {
		Long total = 0L;
		for(long i = 153; i <= 153; i++) {
			String toWritten = Utils.toWritten(i);
			System.out.println(toWritten);
			for(int j = 0; j < toWritten.length(); j++) {
				char ch = toWritten.charAt(j);
				if((int)ch >= 97 && (int)ch <= 122){
					total++;
				}
			}
		}
		return total;
	}
}

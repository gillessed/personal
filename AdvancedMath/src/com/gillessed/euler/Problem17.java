package com.gillessed.euler;

import com.gillessed.euler.utils.Utils;



public class Problem17 implements Problem<Long> {
	@Override
    public Long evaluate() {
		Long total = 0L;
		for(long i = 1; i <= 1000; i++) {
			String toWritten = Utils.toWritten(i);
			System.out.println(toWritten);
			for(int j = 0; j < toWritten.length(); j++) {
				char ch = toWritten.charAt(j);
				if(ch >= 97 && ch <= 122){
					total++;
				}
			}
		}
		return total;
	}
}

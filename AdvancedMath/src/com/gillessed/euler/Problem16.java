package com.gillessed.euler;

import java.math.BigInteger;

import com.gillessed.euler.utils.Utils;




public class Problem16 implements Problem<Long> {
	public Long evaluate() {
		BigInteger constTwo = new BigInteger("2");
		BigInteger num = new BigInteger("1");
		for(int i = 0; i < 1000; i++) {
			num = num.multiply(constTwo);
		}
		String s = num.toString();
		return (long)Utils.digitSum(s);
	}
}

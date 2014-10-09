package com.gillessed.advancedmath.integer;

public class BigInteger {
	
	short values[];
	int size;
	
	public BigInteger(int value) {
		this((long)value);
	}
	
	public BigInteger(short value) {
		this((long)value);
	}
	
	public BigInteger(long value) {
		
	}
	
	public BigInteger(String value) {
		values = new short[value.length()];
	}
}

package com.gillessed.advancedmath.field;

import com.gillessed.advancedmath.integer.IntegerFunctions;

public class RationalNumber implements FieldElement {
	private long numerator, denominator;
	private boolean symbol;
	public RationalNumber() {
		this(0,1);
	}
	public RationalNumber(long numerator, long denominator) {
		if(denominator < 1) {
			throw new IllegalArgumentException("Denominator must be greater than 1");
		}
		if(numerator < 0) {
			throw new IllegalArgumentException("Numerator must be greater than 0");
		}
		this.numerator = Math.abs(numerator);
		this.denominator = Math.abs(denominator);
		reduce();
		symbol = (numerator >= 0) && (denominator >= 0);
	}
	public RationalNumber(RationalNumber r) {
		this.numerator = r.getNumerator();
		this.denominator = r.getDenominator();
	}
	private void reduce() {
		long gcd = IntegerFunctions.gcd(numerator, denominator);
		if(gcd > 1) {
			numerator /= gcd;
			denominator /= gcd;
		}
	}
	public void setNumerator(long numerator) {
		if(numerator < 0) {
			throw new IllegalArgumentException("Numerator must be greater than 0");
		}
		this.numerator = numerator;
		reduce();
	}
	public long getNumerator() {
		return numerator;
	}
	public void setDenominator(long denominator) {
		if(denominator < 1) {
			throw new IllegalArgumentException("Denominator must be greater than 1");
		}
		this.denominator = denominator;
		reduce();
	}
	public long getDenominator() {
		return denominator;
	}
	public void print() {
		if(!symbol) {
			System.out.print("-");
		}
		if(denominator > 1) {
			System.out.print(numerator + "/" + denominator);
		} else {
			System.out.print(numerator);
		}
	}
	public void println() {
		print();
		System.out.println();
	}
	public int getPrintSize() {
		int printSize = 0;
		if(!symbol) printSize++;
		printSize += 1 + (int)(Math.log(numerator) / Math.log(10));
		if(denominator > 1) {
			printSize += (2 + (int)(Math.log(denominator) / Math.log(10)));
		}
		return printSize;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof RationalNumber) {
			RationalNumber r = (RationalNumber)obj;
			return r.getDenominator() == denominator && r.getNumerator() == numerator;
		} else {
			return false;
		}
	}
	@Override
	public int hashCode() {
		int result = 17;
		result += denominator * 31;
		result += numerator * 31;
		return result;
	}
}

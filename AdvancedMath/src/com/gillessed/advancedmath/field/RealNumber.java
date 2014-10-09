package com.gillessed.advancedmath.field;

public class RealNumber implements FieldElement{

	private final Double value;
	private final boolean sign;
	
	public RealNumber(Double value) {
		this.value = value;
		sign = (value < 0) ? false : true;
	}
	@Override
	public void print() {
		System.out.print(getValue());
	}
	@Override
	public void println() {
		print();
		System.out.println();
	}
	@Override
	public int getPrintSize() {
		return getValue().toString().length();
	}
	public Double getValue() {
		return value;
	}
	public boolean isSign() {
		return sign;
	}
}

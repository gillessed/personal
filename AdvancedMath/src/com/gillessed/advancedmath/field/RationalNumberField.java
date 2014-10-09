package com.gillessed.advancedmath.field;


public class RationalNumberField extends Field<RationalNumber> {

	@Override
	public RationalNumber add(RationalNumber e1, RationalNumber e2) {
		long newNumerator = e1.getNumerator() * e2.getDenominator() + e1.getDenominator() * e2.getNumerator();
		long newDenominator = e1.getDenominator() * e2.getDenominator();
		return new RationalNumber(newNumerator, newDenominator);
	}

	@Override
	public RationalNumber multiply(RationalNumber e1, RationalNumber e2) {
		return new RationalNumber(e1.getNumerator() * e2.getNumerator(), e1.getDenominator() * e2.getDenominator());
	}

	@Override
	public RationalNumber multiplicativeInverse(RationalNumber e) {
		return new RationalNumber(e.getDenominator(), e.getNumerator());
	}

	@Override
	public RationalNumber additiveInverse(RationalNumber e) {
		return new RationalNumber(-e.getNumerator(), e.getDenominator());
	}

	@Override
	public RationalNumber getAdditiveIdentity() {
		return new RationalNumber();
	}

	@Override
	public RationalNumber getMultiplicativeIdentity() {
		return new RationalNumber(1,1);
	}

	@Override
	public boolean belongsToField(String s) {
		//TODO: Implement this
		return false;
	}
	
	@Override
	public RationalNumber parseString(String element) {
		//TODO: Implement this
		return null;
	}

	@Override
	public RationalNumber exp(RationalNumber e1, RationalNumber e2) {
		throw new RuntimeException("Rational numbers do not support exponentiation");
	}

	@Override
	public RationalNumber copy(RationalNumber e) {
		return new RationalNumber(e.getNumerator(), e.getDenominator());
	}
	
}

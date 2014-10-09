package com.gillessed.advancedmath.field;

import com.gillessed.advancedmath.function.Predefined;


public class RealNumberField extends Field<RealNumber> {
	
	@Override
	public RealNumber add(RealNumber e1, RealNumber e2) {
		return new RealNumber(e1.getValue() + e2.getValue());
	}
	@Override
	public RealNumber multiply(RealNumber e1, RealNumber e2) {
		return new RealNumber(e1.getValue() * e2.getValue());
	}
	@Override
	public RealNumber exp(RealNumber e1, RealNumber e2) {
		return new RealNumber(Math.pow(e1.getValue(), e2.getValue()));
	}
	@Override
	public RealNumber multiplicativeInverse(RealNumber e) {
		return new RealNumber(1 / e.getValue());
	}
	@Override
	public RealNumber additiveInverse(RealNumber e) {
		return new RealNumber(-e.getValue());
	}
	@Override
	public RealNumber getAdditiveIdentity() {
		return new RealNumber(0.0);
	}
	@Override
	public RealNumber getMultiplicativeIdentity() {
		return new RealNumber(1.0);
	}
	@Override
	public boolean belongsToField(String element) {
		//TODO: Implement this better.
		try {
			Double.parseDouble(element);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	@Override
	public RealNumber parseString(String element) {
		return new RealNumber(Double.parseDouble(element));
	}
	
	@Predefined
	@Override
	public RealNumber abs(RealNumber e) {
		return new RealNumber(Math.abs(e.getValue()));
	}

	@Predefined
	@Override
	public RealNumber cos(RealNumber e) {
		return new RealNumber(Math.cos(e.getValue()));
	}

	@Predefined
	@Override
	public RealNumber sin(RealNumber e) {
		return new RealNumber(Math.sin(e.getValue()));
	}

	@Predefined
	@Override
	public RealNumber tan(RealNumber e) {
		return new RealNumber(Math.tan(e.getValue()));
	}

	@Predefined
	@Override
	public RealNumber arccos(RealNumber e) {
		return new RealNumber(Math.acos(e.getValue()));
	}

	@Predefined
	@Override
	public RealNumber arcsin(RealNumber e) {
		return new RealNumber(Math.asin(e.getValue()));
	}

	@Predefined
	@Override
	public RealNumber arctan(RealNumber e) {
		return new RealNumber(Math.atan(e.getValue()));
	}

	@Predefined
	@Override
	public RealNumber ln(RealNumber e) {
		return new RealNumber(Math.log(e.getValue()));
	}
	@Override
	public RealNumber copy(RealNumber e) {
		return new RealNumber(e.getValue());
	}
	
}

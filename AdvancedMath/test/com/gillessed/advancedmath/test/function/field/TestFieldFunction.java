package com.gillessed.advancedmath.test.function.field;

import java.util.HashMap;
import java.util.Map;

import com.gillessed.advancedmath.field.RealNumber;
import com.gillessed.advancedmath.field.RealNumberField;
import com.gillessed.advancedmath.function.field.FieldFunction;


public class TestFieldFunction {

	public static void main(String[] args) {
		RealNumberField f = new RealNumberField();
		FieldFunction<RealNumber> newFunc = new FieldFunction<RealNumber>(f, "f(x)=(x+1)^2+3.2");
		Map<String, RealNumber> parameters = new HashMap<String, RealNumber>();
		parameters.put("x", new RealNumber(2.1d));
		parameters.put("y", new RealNumber(3.0d));
		newFunc.evaluate(parameters).println();
		
		parameters.clear();
		parameters.put("x", new RealNumber(3.0d));
		parameters.put("y", new RealNumber(4.0d));
		newFunc.evaluate(parameters).println();
	}

}

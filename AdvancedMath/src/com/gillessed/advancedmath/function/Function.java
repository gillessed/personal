package com.gillessed.advancedmath.function;

import java.util.Map;

import com.gillessed.advancedmath.MathObject;


public interface Function<E extends MathObject> extends MathObject {
	public static int INFINITE_INPUTS = -1;
	public E evaluate(Map<String, E> parameters);
	public int getMaxInputs();
}

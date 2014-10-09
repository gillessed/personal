package com.gillessed.advancedmath.function.field;

import java.util.Map;

import com.gillessed.advancedmath.field.Field;
import com.gillessed.advancedmath.field.FieldElement;


public class FieldParenthetical<E extends FieldElement> extends FieldFunction<E> {
	
	private final int inputs;
	public FieldParenthetical(Field<E> field, String definition, FieldFunction<E> parent, int inputs) {
		super(field, definition, parent, "parenthesis", true, true);
		this.inputs = inputs;
	}
	@Override
	public E evaluate(Map<String,E> parameters) {
		checkInputs();
		return subFunctions.get(0).evaluate(parameters);
	}
	@Override
	public int getMaxInputs() {
		return inputs;
	}
}

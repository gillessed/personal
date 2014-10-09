package com.gillessed.advancedmath.function.field;

import java.util.Map;

import com.gillessed.advancedmath.field.Field;
import com.gillessed.advancedmath.field.FieldElement;


public class FieldTan<E extends FieldElement> extends FieldPredefined<E> {

	public FieldTan(String name, Field<E> field, String definition, FieldFunction<E> parent) {
		super(name, field, definition, parent);
	}
	
	@Override
	public E evaluate(Map<String, E> parameters) {
		checkInputs();
		return field.tan(subFunctions.get(0).evaluate(parameters));
	}
}

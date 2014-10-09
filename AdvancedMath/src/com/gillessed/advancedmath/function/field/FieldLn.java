package com.gillessed.advancedmath.function.field;

import java.util.Map;

import com.gillessed.advancedmath.field.Field;
import com.gillessed.advancedmath.field.FieldElement;


public class FieldLn<E extends FieldElement> extends FieldPredefined<E> {

	public FieldLn(String name, Field<E> field, String definition, FieldFunction<E> parent) {
		super(name, field, definition, parent);
	}
	
	@Override
	public E evaluate(Map<String, E> parameters) {
		checkInputs();
		return field.ln(subFunctions.get(0).evaluate(parameters));
	}
}

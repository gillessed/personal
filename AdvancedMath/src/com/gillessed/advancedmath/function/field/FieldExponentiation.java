package com.gillessed.advancedmath.function.field;

import java.util.Map;

import com.gillessed.advancedmath.field.Field;
import com.gillessed.advancedmath.field.FieldElement;


public class FieldExponentiation<E extends FieldElement> extends FieldOperator<E> {
	
	public FieldExponentiation(Field<E> field, String definition, FieldFunction<E> parent) {
		super(field, definition, parent, "exponentiate");
	}
	@Override
	public E evaluate(Map<String,E> parameters) {
		return field.exp(subFunctions.get(0).evaluate(parameters), subFunctions.get(1).evaluate(parameters));
	}
}

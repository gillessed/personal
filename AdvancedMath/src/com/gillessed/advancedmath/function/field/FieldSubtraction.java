package com.gillessed.advancedmath.function.field;

import java.util.Map;

import com.gillessed.advancedmath.field.Field;
import com.gillessed.advancedmath.field.FieldElement;


public class FieldSubtraction<E extends FieldElement> extends FieldOperator<E> {
	
	public FieldSubtraction(Field<E> field, String definition, FieldFunction<E> parent) {
		super(field, definition, parent, "subtract");
	}
	@Override
	public E evaluate(Map<String,E> parameters) {
		return field.add(subFunctions.get(0).evaluate(parameters), field.additiveInverse(subFunctions.get(1).evaluate(parameters)));
	}
}

package com.gillessed.advancedmath.function.field;

import java.util.Map;

import com.gillessed.advancedmath.field.Field;
import com.gillessed.advancedmath.field.FieldElement;


public class FieldVariable<E extends FieldElement> extends FieldFunction<E> {
	
	public FieldVariable(Field<E> field, String definition,  FieldFunction<E> parent) {
		super(field, definition, parent, definition, false, false);
	}
	
	@Override
	public E evaluate(Map<String, E> parameters) {
		return parameters.get(name);
	}

	@Override
	public int getMaxInputs() {
		return 0;
	}

}

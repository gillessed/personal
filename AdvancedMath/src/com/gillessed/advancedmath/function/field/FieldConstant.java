package com.gillessed.advancedmath.function.field;

import java.util.Map;

import com.gillessed.advancedmath.field.Field;
import com.gillessed.advancedmath.field.FieldElement;


public class FieldConstant<E extends FieldElement> extends FieldFunction<E> {
	
	private E constantValue;
	
	public FieldConstant(Field<E> field, String definition, E constantValue, FieldFunction<E> parent) {
		super(field, definition, parent, definition, false, false);
		this.constantValue = constantValue;
	}
	
	@Override
	public E evaluate(Map<String, E> parameters) {
		return constantValue;
	}

	@Override
	public int getMaxInputs() {
		return 0;
	}

}

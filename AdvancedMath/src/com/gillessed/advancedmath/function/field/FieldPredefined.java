package com.gillessed.advancedmath.function.field;

import com.gillessed.advancedmath.field.Field;
import com.gillessed.advancedmath.field.FieldElement;

public class FieldPredefined<E extends FieldElement> extends FieldFunction<E> {

	public FieldPredefined(String name, Field<E> field, String definition, FieldFunction<E> parent) {
		super(field, definition, parent, name, true, true);
	}
	
	@Override
	public int getMaxInputs() {
		return 1;
	}
}

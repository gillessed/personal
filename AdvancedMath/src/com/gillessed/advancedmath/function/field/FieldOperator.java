package com.gillessed.advancedmath.function.field;

import com.gillessed.advancedmath.field.Field;
import com.gillessed.advancedmath.field.FieldElement;

public class FieldOperator<E extends FieldElement> extends FieldFunction<E> {
	public FieldOperator(Field<E> field, String definition, FieldFunction<E> parent, String name) {
		super(field, definition, parent, name, true, false);
	}
	@Override
	public int getMaxInputs() {
		return 2;
	}
}

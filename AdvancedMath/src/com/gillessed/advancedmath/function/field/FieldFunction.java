package com.gillessed.advancedmath.function.field;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gillessed.advancedmath.field.Field;
import com.gillessed.advancedmath.field.FieldElement;
import com.gillessed.advancedmath.function.Function;
import com.gillessed.advancedmath.function.parser.FieldFunctionParser;


public class FieldFunction<E extends FieldElement> implements Function<E> {

	protected final int parameters;
	protected final String name;
	protected final String definition;
	protected final Field<E> field;
	protected final List<FieldFunction<E>> subFunctions;
	protected final FieldFunction<E> parent;
	
	public FieldFunction(Field<E> field, String definition) {
		this.definition = definition.substring(definition.indexOf('=') + 1, definition.length());
		this.field = field;
		this.name = definition.substring(0, definition.indexOf('='));
		this.parameters = name.split(",").length;
		this.parent = null;
			
		subFunctions = new ArrayList<FieldFunction<E>>();
		FieldFunctionParser<E> ffp = new FieldFunctionParser<E>(field, this.definition);
		subFunctions.add(ffp.parseFunction(this));
	}
	
	protected FieldFunction(Field<E> field, String definition, FieldFunction<E> parent, String name, boolean hasChildren, boolean parenthetical) {
		this.definition = definition;
		this.field = field;
		this.name = name;
		this.parent = parent;
		this.parameters = getParameters();
		
		subFunctions = new ArrayList<FieldFunction<E>>();
		if(!parenthetical) {
			if(hasChildren) {
				FieldFunctionParser<E> ffp = new FieldFunctionParser<E>(field, definition);
				subFunctions.addAll(ffp.splitFunction(this));
			}
		} else {
			FieldFunctionParser<E> ffp = new FieldFunctionParser<E>(field, definition);
			subFunctions.addAll(ffp.splitParenthetical(this));
		}
	}
	
	@Override
	public E evaluate(Map<String, E> parameters) {
		if(getMaxInputs() < subFunctions.size()) {
			throw new RuntimeException("You have too many functions.");
		}
		return subFunctions.get(0).evaluate(parameters);
	}
	@Override
	public int getMaxInputs() {
		return 1;
	}
	@Override
	public void print() {
		System.out.println("This is a function.");
	}
	public int getParameters() {
		if(parent == this) {
			throw new RuntimeException("Recursive function parent");
		}
		if(parent != null) {
			return parent.getParameters();
		}
		return parameters;
	}
	protected void checkInputs() {
		if(subFunctions.size() > getMaxInputs()) {
			throw new RuntimeException("Function " + definition + " has too many inputs.");
		}
	}
}

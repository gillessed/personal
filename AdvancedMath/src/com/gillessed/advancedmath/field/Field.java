package com.gillessed.advancedmath.field;

import java.util.HashMap;
import java.util.Map;

import com.gillessed.advancedmath.function.field.FieldAbs;
import com.gillessed.advancedmath.function.field.FieldCos;
import com.gillessed.advancedmath.function.field.FieldFunction;
import com.gillessed.advancedmath.function.field.FieldLn;
import com.gillessed.advancedmath.function.field.FieldSin;
import com.gillessed.advancedmath.function.field.FieldTan;


public abstract class Field <E extends FieldElement> {
	
	private final Map<String, FieldFunction<E>> functionMap = new HashMap<String, FieldFunction<E>>();
	
	public abstract E add(E e1, E e2);
	public abstract E multiply(E e1, E e2);
	public abstract E additiveInverse(E e);
	public abstract E multiplicativeInverse(E e);
	public abstract E getAdditiveIdentity();
	public abstract E getMultiplicativeIdentity();
	public abstract boolean belongsToField(String element);
	public abstract E parseString(String element);
	public abstract E copy(E e);

	public FieldFunction<E> getFunction(String name, Field<E> field, String expr, FieldFunction<E> parent) {
		if(functionMap.containsKey(name)) {
			//TODO: implement user defined functions
			return null;
		} else {
			if("abs".equalsIgnoreCase(name)) {
				return new FieldAbs<E>(name, field, expr, parent);
			} else if("cos".equalsIgnoreCase(name)) {
				return new FieldCos<E>(name, field, expr, parent);
			} else if("sin".equalsIgnoreCase(name)) {
				return new FieldSin<E>(name, field, expr, parent);
			} else if("tan".equalsIgnoreCase(name)) {
				return new FieldTan<E>(name, field, expr, parent);
			} else if("ln".equalsIgnoreCase(name)) {
				return new FieldLn<E>(name, field, expr, parent);
			}
			return null;
		}
	}

	public Map<String, FieldFunction<E>> getFunctionMap() {
		return functionMap;
	}

	public void addToFunctionMap(String name, FieldFunction<E> function) {
		if(functionMap.containsKey(name)) {
			throw new RuntimeException("The function " + name + " is already defined.");
		}
		functionMap.put(name, function);
	}
	
	/*
	 * The following functions are those that only some fields may "support" so
	 * by default they throw an exception, but if the field does "support" it
	 * then one can override it there.
	 */
	public E exp(E e1, E e2) {
		throw new RuntimeException("This field does not support absolute values.");
	}
	public E abs(E e) {
		throw new RuntimeException("This field does not support absolute values.");
	}
	public E cos(E e) {
		throw new RuntimeException("This field does not support absolute values.");
	}
	public E sin(E e) {
		throw new RuntimeException("This field does not support absolute values.");
	}
	public E tan(E e) {
		throw new RuntimeException("This field does not support absolute values.");
	}
	public E arccos(E e) {
		throw new RuntimeException("This field does not support absolute values.");
	}
	public E arcsin(E e) {
		throw new RuntimeException("This field does not support absolute values.");
	}
	public E arctan(E e) {
		throw new RuntimeException("This field does not support absolute values.");
	}
	public E cosh(E e) {
		throw new RuntimeException("This field does not support absolute values.");
	}
	public E sinh(E e) {
		throw new RuntimeException("This field does not support absolute values.");
	}
	public E tanh(E e) {
		throw new RuntimeException("This field does not support absolute values.");
	}
	public E arccosh(E e) {
		throw new RuntimeException("This field does not support absolute values.");
	}
	public E arcsinh(E e) {
		throw new RuntimeException("This field does not support absolute values.");
	}
	public E arctanh(E e) {
		throw new RuntimeException("This field does not support absolute values.");
	}
	public E ln(E e) {
		throw new RuntimeException("This field does not support absolute values.");
	}
}

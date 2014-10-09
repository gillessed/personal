package com.gillessed.advancedmath.vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gillessed.advancedmath.field.Field;
import com.gillessed.advancedmath.field.FieldElement;


public class FieldVector<T extends FieldElement> {
	
	private final List<T> elements;
	private final int size;
	private final Field<T> field;
	
	@SuppressWarnings("unchecked")
	public FieldVector(Field<T> field, int size) {
		this(field, size, (List<T>)Collections.emptyList());
	}
	
	public FieldVector(Field<T> field, int size, List<T> inputs) {
		this.size = size;
		this.field = field;
		elements = new ArrayList<T>();
		for(T element : inputs) {
			elements.add(element);
		}
	}
	public T get(int elementIndex) {
		return elements.get(elementIndex);
	}
	public void put(int elementIndex, T element) {
		elements.set(elementIndex, element);
	}
	public FieldVector<T> add(FieldVector<T> v) {
		if(size != v.getSize()) {
			throw new RuntimeException("When adding 2 vectors, they must have the same size.");
		}
		List<T> newElements = new ArrayList<T>();
		for(int i = 0; i < size; i++) {
			newElements.add(field.add(get(i), v.get(i)));
		}
		return new FieldVector<T>(field, size, newElements);
	}
	public T dot(FieldVector<T> v) {
		if(size != v.getSize()) {
			throw new RuntimeException("When dotting 2 vectors, they must have the same size.");
		}
		T element = field.getAdditiveIdentity();
		for(int i = 0; i < v.size; i++) {
			element = field.add(element, field.multiply(v.get(i), get(i)));
		}
		return element;
	}
	public int getSize() {
		return size;
	}
}

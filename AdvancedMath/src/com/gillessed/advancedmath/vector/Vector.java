package com.gillessed.advancedmath.vector;

import com.gillessed.advancedmath.matrix.Matrix;


public class Vector {
	
	public static Vector getZero(int size) {
		return new Vector(size);
	}
	
	protected final double[] elements;
	protected final int size;
	
	public Vector(int size) {
		this(size, new Double[] {});
	}
	
	public Vector(int size, Double... inputs) {
		this.size = size;
		elements = new double[size];
		for(int i = 0 ; i < inputs.length; i++) {
			elements[i] = inputs[i];
		}
	}
	public Vector(Matrix m) {
		if(m.getColumns() != 1) { 
			throw new RuntimeException("When making a vector out of a matrix, that matrix must have one column");
		}
		size = m.getRows();
		elements = new double[size];
		for(int i = 0; i < size; i++) {
			elements[i] = m.getElement(i + 1, 1);
		}
	}

	public double get(int elementIndex) {
		return elements[elementIndex - 1];
	}
	public void put(int elementIndex, double element) {
		elements[elementIndex - 1] = element;
	}
	public Vector add(Vector v) {
		if(size != v.getSize()) {
			throw new RuntimeException("When adding 2 vectors, they must have the same size.");
		}
		Vector newVector = new Vector(size);
		for(int i = 1; i <= size; i++) {
			newVector.put(i, get(i) + v.get(i));
		}
		
		return newVector;
	}
	public Vector subtract(Vector v) {
		if(size != v.getSize()) {
			throw new RuntimeException("When adding 2 vectors, they must have the same size.");
		}
		Vector newVector = new Vector(size);
		for(int i = 1; i <= size; i++) {
			newVector.put(i, get(i) - v.get(i));
		}
		
		return newVector;
	}
	public Vector multiply(double scalar) {
		Vector v = new Vector(size);
		for(int i = 1; i <= size; i++) {
			v.put(i, get(i) * scalar);
		}
		return v;
	}
	public double dot(Vector v) {
		if(size != v.getSize()) {
			throw new RuntimeException("When dotting 2 vectors, they must have the same size.");
		}
		double sum = 0;
		for(int i = 0; i < size; i++) {
			sum += get(i) * v.get(i);
		}
		return sum;
	}
	public int getSize() {
		return size;
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Vector)) {
			return false;
		}
		Vector v = (Vector)obj;
		if(size != v.getSize()) {
			return false;
		}
		for(int i = 1; i <= size; i++) {
			if(get(i) != v.get(i)) return false;
		}
		return true;
	}
	@Override
	public int hashCode() {
		int hash = 17;
		int result = 0;
		result += hash * 31 + size;
		result += hash * 31 + elements.hashCode();
		return result;
	}
	public void print() { print(""); }
	public void print(String s) {
		if(s.length() != 0)
			System.out.println(s);
		for(int i = 1; i <= size; i++) {
			System.out.println(get(i));
		}
	}
	@Override
	public String toString() {
		String s = "(" + elements[0];
		for(int i = 1; i < size; i++) {
			s += "," + elements[i];
		}
		return s + ")";
	}
}

package com.gillessed.advancedmath.matrix;

import java.util.ArrayList;
import java.util.List;

import com.gillessed.advancedmath.MathObject;
import com.gillessed.advancedmath.field.Field;
import com.gillessed.advancedmath.field.FieldElement;


public class FieldMatrix <T extends FieldElement> implements MathObject {
	private List<List<T>> elements;
	private Field<T> field;
	private final int columns;
	private final int rows;
	public FieldMatrix(int rows, int columns, Field<T> field) {
		this.field = field;
		this.columns = columns;
		this.rows = rows;
		elements = new ArrayList<List<T>>();
		for(int i = 0; i < rows; i++) {
			List<T> temp = new ArrayList<T>();
			for(int j = 0; j < columns; j++) {
				temp.add(field.getAdditiveIdentity());
			}
			elements.add(temp);
		}
	}
	public T getElement(int row, int column) {
		return elements.get(row - 1).get(column - 1);
	}
	public void setElement(int row, int column, T element) {
		elements.get(row - 1).set(column - 1, element);
	}
	public FieldMatrix<T> add(FieldMatrix<T> m) {
		if(!m.getField().getClass().equals(field.getClass())) {
			throw new IllegalArgumentException("m does not have the same field as your current matrix");
		}
		if(columns != m.getColumns()) {
			throw new IllegalArgumentException("m does not have the same width as your current matrix");
		}
		if(rows != m.getRows()) {
			throw new IllegalArgumentException("m does not have the same height as your current matrix");
		}
		FieldMatrix<T> ret = new FieldMatrix<T>(columns, rows, field);
		for(int i = 1; i <= columns; i++) {
			for(int j = 1; j <= rows; j++) {
				ret.setElement(i,j,
						field.add(getElement(i,j),
								m.getElement(i, j)));
			}
		}
		return ret;
	}
	public FieldMatrix<T> multiply(FieldMatrix<T> m) {
		if(rows != m.getColumns()) {
			throw new IllegalArgumentException("m does not have the same height as your current matrix width");
		}
		FieldMatrix<T> ret = new FieldMatrix<T>(columns, m.getRows(), getField());
		for(int i = 1; i <= rows; i++) {
			for(int j = 1; j <= columns; j++) {
				ret.setElement(i,j,
						getField().add(getElement(i,j),
								m.getElement(i,j)));
			}
		}
		return ret;
	}
	public int getColumns() {
		return columns;
	}
	public int getRows() {
		return rows;
	}
	public void print() {
		for(int i = 1; i <= rows; i++) {
			if(i == 1 || i == rows) { 
				System.out.print("[ ");
			} else {
				System.out.print("| ");
			}
			for(int j = 1; j <= columns; j++) {
				getElement(i,j).print();
				System.out.print(" ");
			}
			if(i == 1 || i == rows) { 
				System.out.print("]");
			} else {
				System.out.print("|");
			}
			System.out.println();
		}
	}
	public Field<T> getField() {
		return field;
	}
}

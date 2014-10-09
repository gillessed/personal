package com.gillessed.advancedmath.matrix;

import com.gillessed.advancedmath.vector.Vector;


public class Matrix {
	private double elements[][];
	private final int columns;
	private final int rows;
	public Matrix(int rows, int columns) {
		this.columns = columns;
		this.rows = rows;
		elements = new double[rows][columns];
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				elements[i][j] = 0.0;
			}
		}
	}
	public Matrix(Vector v) {
		columns = 1;
		rows = v.getSize();
		elements = new double[rows][columns];
		for(int i = 0; i < rows; i++) {
			elements[i][0] = rows;
		}
	}
	public double getElement(int row, int column) {
		return elements[row - 1][column - 1];
	}
	public void setElement(int row, int column, double element) {
		elements[row - 1][column - 1] = element;
	}
	public Matrix add(Matrix m) {
		if(columns != m.getColumns()) {
			throw new IllegalArgumentException("m does not have the same width as your current matrix");
		}
		if(rows != m.getRows()) {
			throw new IllegalArgumentException("m does not have the same height as your current matrix");
		}
		Matrix ret = new Matrix(columns, rows);
		for(int i = 1; i <= columns; i++) {
			for(int j = 1; j <= rows; j++) {
				ret.setElement(j,i,getElement(i,j) + m.getElement(i, j));
			}
		}
		return ret;
	}
	public Matrix multiply(Matrix m) {
		if(columns != m.getRows()) {
			throw new IllegalArgumentException("m does not have the same height as your current matrix width");
		}
		Matrix ret = new Matrix(columns, m.getRows());
		for(int i = 1; i <= rows; i++) {
			for(int j = 1; j <= m.getColumns(); j++) {
				double sum = 0;
				for(int k = 1; k <= columns; k++) {
					sum += getElement(i, k) * m.getElement(k, j);
				}
				ret.setElement(i, j, sum);
			}
		}
		return ret;
	}
	public Vector multiply(Vector v) {
		Matrix m1 = new Matrix(v);
		Matrix m2 = multiply(m1);
		return new Vector(m2);
	}
	public int getColumns() {
		return columns;
	}
	public int getRows() {
		return rows;
	}
	public void print() {
		print("");
	}
	public void print(String s) {
		if(s.length() != 0) {
			System.out.println(s);
		}
		for(int i = 1; i <= rows; i++) {
			if(i == 1 || i == rows) { 
				System.out.print("[ ");
			} else {
				System.out.print("| ");
			}
			for(int j = 1; j <= columns; j++) {
				System.out.print(getElement(i,j));
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
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Matrix)) {
			return false;
		}
		Matrix m = (Matrix)obj;
		if(rows != m.getRows() || columns != m.getColumns()) {
			return false;
		}
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				if(getElement(i+1, j+1) != m.getElement(i+1, j+1)) {
					return false;
				}
			}
		}
		return true;
	}
	@Override
	public int hashCode() {
		int hash = 17;
		int result = hash * 31 + columns;
		result = hash * 31 + rows;
		result = hash * 31 + elements.hashCode();
		return result;
	}
}

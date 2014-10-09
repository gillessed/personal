package com.gillessed.advancedmath.matrix;

import com.gillessed.advancedmath.vector.Vector3;

public class Matrix3 extends Matrix {

	/**
	 * Creates a rotation matrix that represents a rotation on the X axis in a standard XYZ co-ordinate system.
	 */
	public static Matrix3 getXRotation(double theta) {
		Matrix3 m = new Matrix3();
		m.setElement(1,1,1);
		m.setElement(1,2,0);
		m.setElement(1,3,0);
		m.setElement(2,1,0);
		m.setElement(2,2,Math.cos(theta));
		m.setElement(2,3,-Math.sin(theta));
		m.setElement(3,1,0);
		m.setElement(3,2,Math.sin(theta));
		m.setElement(3,3,Math.cos(theta));
		return m;
	}

	/**
	 * Creates a rotation matrix that represents a rotation on the Y axis in a standard XYZ co-ordinate system.
	 */
	public static Matrix3 getYRotation(double theta) {
		Matrix3 m = new Matrix3();
		m.setElement(1,1,Math.cos(theta));
		m.setElement(1,2,0);
		m.setElement(1,3,Math.sin(theta));
		m.setElement(2,1,0);
		m.setElement(2,2,1);
		m.setElement(2,3,0);
		m.setElement(3,1,-Math.sin(theta));
		m.setElement(3,2,0);
		m.setElement(3,3,Math.cos(theta));
		return m;
	}

	/**
	 * Creates a rotation matrix that represents a rotation on the Z axis in a standard XYZ co-ordinate system.
	 */
	public static Matrix3 getZRotation(double theta) {
		Matrix3 m = new Matrix3();
		m.setElement(1,1,Math.cos(theta));
		m.setElement(1,2,-Math.sin(theta));
		m.setElement(1,3,0);
		m.setElement(2,1,Math.sin(theta));
		m.setElement(2,2,Math.cos(theta));
		m.setElement(2,3,0);
		m.setElement(3,1,0);
		m.setElement(3,2,0);
		m.setElement(3,3,1);
		return m;
	}
	
	public Matrix3() {
		super(3,3);
	}
	
	public Matrix3(Matrix m) {
		super(3,3);
		if(m.getRows() != 3 || m.getColumns() != 3) {
			throw new RuntimeException("The input matrix must have 3 columns and 3 rows");
		}
		for(int i = 1; i <= 3; i++) {
			for(int j = 1; j <= 3; j++) {
				setElement(i, j, m.getElement(i, j));
			}
		}
	}
	
	public Vector3 multiply(Vector3 v) {
		double elem[] = new double[3];
		for(int i = 1; i <= 3; i++) {
			int sum = 0;
			for(int k = 1; k <= 3; k++) {
				sum += getElement(i, k) * v.get(i);
			}
			elem[i-1] = sum;
		}
		Vector3 ret = new Vector3(elem[0], elem[1], elem[2]);
		return ret;
	}
	
	public Matrix3 add(Matrix3 m) {
		return new Matrix3(super.add(m));
	}
	
	public Matrix3 multiply(Matrix3 m) {
		return new Matrix3(super.multiply(m));
	}
}

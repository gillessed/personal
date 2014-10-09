package com.gillessed.advancedmath.test.matrix;

import com.gillessed.advancedmath.matrix.Matrix;

import junit.framework.TestCase;

public class MatrixTest extends TestCase {
	public void testMatrixPrint() {
		Matrix m1 = new Matrix(3,3);
		Matrix m2 = new Matrix(3,3);
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				m1.setElement(i+1, j+1, i+j);
				m2.setElement(i+1, j+1, i-j);
			}
		}
		Matrix m3 = new Matrix(3,3);
		m3.setElement(1, 1, 5);
		m3.setElement(1, 2, 2);
		m3.setElement(1, 3, -1);
		m3.setElement(2, 1, 8);
		m3.setElement(2, 2, 2);
		m3.setElement(2, 3, -4);
		m3.setElement(3, 1, 11);
		m3.setElement(3, 2, 2);
		m3.setElement(3, 3, -7);
		Matrix m4 = m1.multiply(m2);
		assertEquals(m4, m3);
	}
}

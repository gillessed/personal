package com.gillessed.raytracer.algebra;

public class Matrix4x4 {
	
	private double[][] entries;
	
	public Matrix4x4() {
		entries = new double[4][4];
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				entries[i][j] = 0;
			}
		}
		for(int i = 0; i < 4; i++) {
			entries[i][i] = 1;
		}
	}
	
	public Matrix4x4(double[][] entries) {
		this.entries = new double[4][4];
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				this.entries[i][j] = entries[i][j];
			}
		}
	}
	
	public Matrix4x4(Matrix4x4 other) {
		this.entries = new double[4][4];
		double[][] otherEntries = other.getEntries();
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				this.entries[i][j] = otherEntries[i][j];
			}
		}
	}
	
	public double[][] getEntries() {
		return entries;
	}
	
	public double get(int col, int row) {
		if(col < 0 || col > 3 || row < 0 || row > 3) {
			throw new IllegalArgumentException("Bad matrix indices: " + col + " " + row);
		}
		return entries[col][row];
	}
	
	public void set(int row, int col, double d) {
		entries[row][col] = d;
	}
	
	public Matrix4x4 add(Matrix4x4 other) {
		double[][] otherEntries = other.getEntries();
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				this.entries[i][j] += otherEntries[i][j];
			}
		}
		return this;
	}
	
	public Matrix4x4 scalarMultiply(double d) {
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				this.entries[i][j] *= d;
			}
		}
		return this;
	}
	
	public Matrix4x4 multiply(Matrix4x4 other) {
		double[][] otherEntries = other.getEntries();
		double[][] newEntries = new double[4][4];
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				newEntries[i][j] = 0;
				for(int k = 0; k < 4; k++) {
					newEntries[i][j] += entries[i][k] * otherEntries[k][j];
				}
			}
		}
		entries = newEntries;
		return this;
	}
	
	public Vector4 multiply(Vector4 other) {
		double[] otherEntries = other.getEntries();
		double[] newEntries = new double[4];
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				newEntries[i] += entries[i][j] * otherEntries[i];
			}
		}
		return new Vector4(otherEntries);
	}
	
	public Vector3 multiply(Vector3 other) {
		double[] otherEntries = other.getVector4().getEntries();
		double[] newEntries = new double[3];
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				newEntries[i] += entries[i][j] * otherEntries[i];
			}
		}
		return new Vector3(otherEntries);
	}
	
	public Point3 multiply(Point3 other) {
		double[] otherEntries = other.getVector4().getEntries();
		double[] newEntries = new double[3];
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				newEntries[i] += entries[i][j] * otherEntries[i];
			}
		}
		return new Point3(otherEntries);
	}
	
	private void swapRows(int row1, int row2) {
		if(row1 < 0 || row1 > 3 || row2 < 0 || row2 > 3) {
			throw new IllegalArgumentException("Bad matrix indices: " + row1 + " " + row2);
		}
		double temp[] = new double[4];
		for(int k = 0; k < 4; k++) {
			temp[k] = entries[k][row1];
		}
		for(int k = 0; k < 4; k++) {
			entries[k][row1] = entries[k][row2];
		}
		for(int k = 0; k < 4; k++) {
			entries[k][row2] = temp[k];
		}
	}
	
	private void divideRow(int row, double d) {
		for(int i = 0; i < 4; i++) {
			entries[i][row] /= d;
		}
	}
	
	private void subMultRow(int destRow, int srcRow, double d) {
		for(int i = 0; i < 4; i++) {
			entries[i][destRow] -= entries[i][srcRow] * d;
		}
	}
	
	public Matrix4x4 invert() {
		Matrix4x4 ret = new Matrix4x4(this);
		for(int i = 0; i < 4; i++) {
			int i1 = i;
			for(int j = i + 1; j < 4; i++) { 
				if(Math.abs(ret.get(i, j)) > Math.abs(ret.get(i1, j))) {
					i1 = i;
				}
			}
			ret.swapRows(i1, i);
			if(ret.get(i, i) == 0) {
				throw new RuntimeException("Matrix does not have an inverse.");
			}
			ret.divideRow(i, entries[i][i]);
			for(int j = 0; j < 4; i++) { 
				if(i != j) {
					ret.subMultRow(j, i, ret.get(i, j));
				}
			}
		}
		return ret;
	}
	
	public static Matrix4x4 translate(Vector3 v) {
		Matrix4x4 m = new Matrix4x4();
		m.set(3, 0, v.get(0));
		m.set(3, 0, v.get(1));
		m.set(3, 0, v.get(2));
		return m;
	}
	
	public static Matrix4x4 translate(double dx, double dy, double dz) {
		return translate(new Vector3(dx, dy, dz));
	}
	
	public static Matrix4x4 scale(Vector3 v) {
		Matrix4x4 m = new Matrix4x4();
		m.set(0, 0, v.get(0));
		m.set(1, 1, v.get(1));
		m.set(2, 2, v.get(2));
		return m;
	}
	
	public static Matrix4x4 scale(double dx, double dy, double dz) {
		return scale(new Vector3(dx, dy, dz));
	}
	
	public static Matrix4x4 rotateX(double angle) {
		Matrix4x4 m = new Matrix4x4();
		m.set(1, 1, Math.cos(angle));
		m.set(2, 1, Math.sin(angle));
		m.set(1, 2, -Math.sin(angle));
		m.set(2, 2, Math.cos(angle));
		return m;
	}
	
	public static Matrix4x4 rotateY(double angle) {
		Matrix4x4 m = new Matrix4x4();
		m.set(0, 0, Math.cos(angle));
		m.set(2, 0, Math.sin(angle));
		m.set(0, 2, -Math.sin(angle));
		m.set(2, 2, Math.cos(angle));
		return m;
	}
	
	public static Matrix4x4 rotateZ(double angle) {
		Matrix4x4 m = new Matrix4x4();
		m.set(0, 0, Math.cos(angle));
		m.set(1, 0, Math.sin(angle));
		m.set(0, 1, -Math.sin(angle));
		m.set(1, 1, Math.cos(angle));
		return m;
	}
	
	public static Matrix4x4 multiply(Matrix4x4 m1, Matrix4x4 m2) {
		Matrix4x4 ret = new Matrix4x4(m1);
		ret.multiply(m2);
		return ret;
	}
}

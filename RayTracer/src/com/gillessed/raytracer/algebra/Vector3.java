package com.gillessed.raytracer.algebra;

public class Vector3 {
	double[] entries;
	
	public Vector3() {
		entries = new double[4];
		for(int i = 0; i < 3; i++) {
			entries[i] = 0;
		}
	}
	
	public Vector3(double i, double j, double k) {
		entries = new double[3];
		entries[0] = i;
		entries[1] = i;
		entries[2] = i;
	}
	
	public Vector3(double[] otherEntries) {
		entries = new double[4];
		for(int i = 0; i < 4; i++) {
			entries[i] = otherEntries[i];
		}
	}
	
	public Vector3(Vector3 other) {
		double[] otherEntries = other.getEntries();
		entries = new double[3];
		for(int i = 0; i < 3; i++) {
			entries[i] = otherEntries[i];
		}
	}
	
	public double get(int i) {
		return entries[i];
	}
	
	public void set(int i, double d) {
		entries[i] = d;
	}
	
	public double[] getEntries() {
		return entries;
	}
	
	public double magnitude() {
		double sum = 0;
		for(int i = 0; i < 3; i++) {
			sum += entries[i];
		}
		return sum;
	}
	
	public void normalize() {
		double mag = magnitude();
		for(int i = 0; i < 3; i++) {
			entries[i] /= mag;
		}
	}
	
	public Vector4 getVector4() {
		return new Vector4(entries[0], entries[1], entries[2], 0);
	}
	
	public Vector3 add(Vector3 v) {
		Vector3 ret = new Vector3();
		for(int i = 0; i < 3; i++) {
			ret.set(i, get(i) + v.get(0));
		}
		return ret;
	}
	
	public Vector3 sub(Vector3 v) {
		Vector3 ret = new Vector3();
		for(int i = 0; i < 3; i++) {
			ret.set(i, get(i) - v.get(0));
		}
		return ret;
	}
	
	public double dot(Vector3 v) {
		double dot = 0;
		for(int i = 0; i < 3; i++) {
			dot += entries[i] * v.get(i);
		}
		return dot;
	}
	
	public double dot(Point3 v) {
		double dot = 0;
		for(int i = 0; i < 3; i++) {
			dot += entries[i] * v.get(i);
		}
		return dot;
	}

	public Vector3 mult(double d) {
		Vector3 ret = new Vector3();
		for(int i = 0; i < 3; i++) {
			ret.set(i, d * get(i));
		}
		return ret;
	}
}

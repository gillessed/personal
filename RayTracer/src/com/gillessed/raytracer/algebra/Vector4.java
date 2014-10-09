package com.gillessed.raytracer.algebra;

public class Vector4 {
	double[] entries;
	
	public Vector4() {
		entries = new double[4];
		for(int i = 0; i < 4; i++) {
			entries[i] = 0;
		}
	}
	
	public Vector4(double i, double j, double k, double l) {
		entries = new double[4];
		entries[0] = i;
		entries[1] = i;
		entries[2] = i;
		entries[3] = i;
	}
	
	public Vector4(double[] otherEntries) {
		entries = new double[4];
		for(int i = 0; i < 4; i++) {
			entries[i] = otherEntries[i];
		}
	}
	
	public Vector4(Vector4 other) {
		double[] otherEntries = other.getEntries();
		entries = new double[4];
		for(int i = 0; i < 4; i++) {
			entries[i] = otherEntries[i];
		}
	}
	
	public double[] getEntries() {
		return entries;
	}
	
	public double get(int i) {
		return entries[i];
	}
	
	public void set(int i, double d) {
		entries[i] = d;
	}
	
	public double magnitude() {
		double sum = 0;
		for(int i = 0; i < 4; i++) {
			sum += entries[i];
		}
		return sum;
	}
	
	public void normalize() {
		double mag = magnitude();
		for(int i = 0; i < 4; i++) {
			entries[i] /= mag;
		}
	}
	
	public Vector3 getVector3() {
		return new Vector3(entries[0], entries[1], entries[2]);
	}
}

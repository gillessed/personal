package com.gillessed.raytracer.algebra;

public class Point3 {
double[] entries;
	
	public Point3() {
		entries = new double[4];
		for(int i = 0; i < 3; i++) {
			entries[i] = 0;
		}
	}
	
	public Point3(double i, double j, double k) {
		entries = new double[3];
		entries[0] = i;
		entries[1] = i;
		entries[2] = i;
	}
	
	public Point3(double[] otherEntries) {
		entries = new double[4];
		for(int i = 0; i < 4; i++) {
			entries[i] = otherEntries[i];
		}
	}
	
	public Point3(Point3 other) {
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
		return new Vector4(entries[0], entries[1], entries[2], 1);
	}
	
	public Point3 add(Vector3 v) {
		Point3 ret = new Point3();
		for(int i = 0; i < 3; i++) {
			ret.set(i, get(i) + v.get(0));
		}
		return ret;
	}
	
	public Vector3 sub(Point3 v) {
		Vector3 ret = new Vector3();
		for(int i = 0; i < 3; i++) {
			ret.set(i, get(i) - v.get(0));
		}
		return ret;
	}
}

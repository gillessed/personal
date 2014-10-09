package com.gillessed.advancedmath.vector;


public class Vector3 extends Vector {
	
	public static Vector3 getZero() {
		return new Vector3();
	}
	
	public Vector3() {
		this(0,0,0);
	}
	
	public Vector3(Vector v) {
		this();
		if(v.getSize() != 3) {
			throw new RuntimeException("The input vector must have size 3");
		}
		for(int i = 1; i <= 3; i++) {
			put(i, v.get(i));
		}
	}
	
	public Vector3(double x, double y, double z) {
		super(3, x, y, z);
	}
	
	public Vector3 cross(Vector3 v) {
		return new Vector3(getY() * v.getZ() - getZ() * v.getY(),
				getZ() * v.getX() - getX() * v.getZ(),
				getX() * v.getY() - getY() * v.getX());
	}
	
	public double getX() {
		return get(1);
	}
	
	public double getY() {
		return get(2);
	}
	
	public double getZ() {
		return get(3);
	}
	
	public Vector3 add(Vector3 v) {
		return new Vector3(super.add(v));
	}
	
	public Vector3 subtract(Vector3 v) {
		return new Vector3(super.subtract(v));
	}
	
	public Vector3 multiply(double scalar) {
		return new Vector3(super.multiply(scalar));
	}
}

package com.gillessed.advancedmath.vector;


public class Vector2 extends Vector {
	
	public static Vector2 getZero() {
		return new Vector2();
	}
	
	public Vector2() {
		this(0,0);
	}
	
	public Vector2(double x, double y) {
		super(0, x, y);
	}
	
	public double getX() {
		return get(0);
	}
	
	public double getY() {
		return get(1);
	}
}

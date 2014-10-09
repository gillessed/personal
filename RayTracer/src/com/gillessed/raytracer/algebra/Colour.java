package com.gillessed.raytracer.algebra;

public class Colour {
	double r;
	double g;
	double b;
	
	public Colour() {
		r = 0;
		g = 0;
		b = 0;
	}
	
	public Colour(double r, double g, double b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Colour(Colour other) {
		this.r = other.getR();
		this.g = other.getG();
		this.b = other.getB();
	}
	
	public double getR() {
		return r;
	}
	
	public double getG() {
		return g;
	}
	
	public double getB() {
		return b;
	}
	
	public Colour add(Colour other) {
		this.r += other.getR();
		this.g += other.getG();
		this.b += other.getB();
		return this;
	}
}

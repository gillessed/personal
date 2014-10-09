package com.gillessed.raytracer.algebra;

public class Light {
	private final Colour colour;
	private final Vector3 position;
	private final double[] falloff;
	
	public Light(Colour colour, Vector3 position, double[] falloff) {
		this.colour = colour;
		this.position = new Vector3(position);
		this.falloff = new double[3];
		for(int i = 0; i < 3; i++) {
			this.falloff[i] = falloff[i];
		}
	}

	public Colour getColour() {
		return colour;
	}

	public Vector3 getPosition() {
		return position;
	}

	public double[] getFalloff() {
		return falloff;
	}
}

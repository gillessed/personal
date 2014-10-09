package net.gillessed.icarus.geometry;

public class IdentityTransformation implements Transformation {

	@Override
	public Point transform(Point z) {
		return z;
	}
	
}

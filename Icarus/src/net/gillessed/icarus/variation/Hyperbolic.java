package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

public class Hyperbolic extends Variation {

	public Hyperbolic() {
		super();
	}
	@Override
	public Point calculate(double inx, double iny) {
		double x;
		double y;
		
		double r = getR(inx,iny);
		double theta = getTheta(inx,iny);
		
		x = Math.sin(theta) / r;
		y = Math.cos(theta) * r;
		
		Point retval = new Point(x, y);
		return retval;
	}
}

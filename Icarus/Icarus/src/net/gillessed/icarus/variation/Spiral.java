package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

public class Spiral extends Variation {

	public Spiral() {
		super();
	}
	@Override
	public Point calculate(double inx, double iny) {
		double x;
		double y;
		
		double r = getR(inx,iny);
		double theta = getTheta(inx,iny);
		
		x = (Math.cos(theta) + Math.sin(r)) / r;
		y = (Math.sin(theta) - Math.cos(r)) / r;
		
		Point retval = new Point(x, y);
		return retval;
	}
}

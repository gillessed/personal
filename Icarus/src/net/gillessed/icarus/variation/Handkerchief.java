package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

public class Handkerchief extends Variation {

	public Handkerchief() {
		super();
	}
	@Override
	public Point calculate(double inx, double iny) {
		double x;
		double y;
		
		double theta = getTheta(inx, iny);
		double r = getR(inx,iny);
		
		x = r * Math.sin(theta + r);
		y = r * Math.cos(theta - r);
		
		Point retval = new Point(x, y);
		return retval;
	}
}

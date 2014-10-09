package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

public class Heart extends Variation {

	public Heart() {
		super();
	}
	@Override
	public Point calculate(double inx, double iny) {
		double x;
		double y;
		
		double r = getR(inx, iny);
		double theta = getTheta(inx, iny);
		
		x = r * Math.sin(theta * r);
		y = -r * Math.cos(theta * r);
		
		Point retval = new Point(x, y);
		return retval;
	}

}

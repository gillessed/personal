package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

public class Greg3 extends Variation {

	public Greg3() {
		super();
	}
	@Override
	public Point calculate(double inx, double iny) {
		double x;
		double y;
		
		double theta = getTheta(inx, iny);
		double r = getR(inx, iny);
		
		x = inx * (r - 0.01) / 2;
		y = inx * theta + (Math.cos(r) - Math.cos(inx));
		
		Point retval = new Point(x, y);
		return retval;
	}
}

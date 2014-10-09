package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

public class Fisheye extends Variation {

	public Fisheye() {
		super();
	}
	@Override
	public Point calculate(double inx, double iny) {
		double x;
		double y;
		
		double r = getR(inx, iny);
		
		x = 2 / (r + 1) * iny;
		y = 2 / (r + 1) * inx;
		
		Point retval = new Point(x, y);
		return retval;
	}
}

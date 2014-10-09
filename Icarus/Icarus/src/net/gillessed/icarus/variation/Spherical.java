package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

public class Spherical extends Variation {

	public Spherical() {
		super();
	}
	@Override
	public Point calculate(double inx, double iny) {
		double x;
		double y;
		
		double r = getR(inx,iny);
		
		x = inx / (r * r);
		y = iny / (r * r);
		
		Point retval = new Point(x, y);
		return retval;
	}
}

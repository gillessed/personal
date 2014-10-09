package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

public class Swirl extends Variation {

	public Swirl() {
		super();
	}
	@Override
	public Point calculate(double inx, double iny) {
		double x;
		double y;
		
		double r = getR(inx,iny);
		
		x = inx * Math.sin(r * r) - iny * Math.cos(r * r);
		y = inx * Math.cos(r * r) + iny * Math.sin(r * r);
		
		Point retval = new Point(x, y);
		return retval;
	}
}

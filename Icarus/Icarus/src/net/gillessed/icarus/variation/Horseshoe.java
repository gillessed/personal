package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

public class Horseshoe extends Variation {

	public Horseshoe() {
		super();
	}
	@Override
	public Point calculate(double inx, double iny) {
		double x;
		double y;
		
		double r = getR(inx,iny);
		
		x = (inx + iny) * (inx - iny) / r;
		y = 2 * inx * iny / r;
		
		Point retval = new Point(x, y);
		return retval;
	}
}

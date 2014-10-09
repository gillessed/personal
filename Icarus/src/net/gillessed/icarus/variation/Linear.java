package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

public class Linear extends Variation {

	public Linear() {
		super();
	}
	@Override
	public Point calculate(double inx, double iny) {
		double x;
		double y;
		
		x = inx;
		y = inx;
		
		Point retval = new Point(x,y);
		return retval;
	}
}

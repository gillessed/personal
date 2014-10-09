package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

public class Greg1 extends Variation {

	public Greg1() {
		super();
	}
	@Override
	public Point calculate(double inx, double iny) {
		double x;
		double y;
		
		x = (Math.cos(inx) + inx) / 1.618;
		y = (Math.sin(iny) + iny) / 1.618;
		
		Point retval = new Point(x, y);
		return retval;
	}
}

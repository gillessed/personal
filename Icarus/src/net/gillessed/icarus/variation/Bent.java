package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

public class Bent extends Variation {

	public Bent() {
		super();
	}
	@Override
	public Point calculate(double inx, double iny) {
		double x;
		double y;
		
		if(inx >= 0) {
			x = inx;
		} else {
			x = 2 * inx;
		}
		if(iny >= 0) {
			y = iny;
		} else {
			y = iny / 2;
		}
		
		Point retval = new Point(x, y);
		return retval;
	}
}

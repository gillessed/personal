package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

public class Sinusoidal extends Variation {

	public Sinusoidal() {
		super();
	}
	@Override
	public Point calculate(double inx, double iny) {
		double x;
		double y;
		
		x = Math.sin(inx);
		y = Math.sin(iny);
		
		Point retval = new Point(x, y);
		return retval;
	}

}

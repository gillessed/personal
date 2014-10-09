package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

public class Exponential extends Variation {

	public Exponential() {
		super();
	}
	@Override
	public Point calculate(double inx, double iny) {
		double x;
		double y;
		
		double exp = Math.exp(inx - 1);
		
		x = exp * Math.cos(Math.PI * inx);
		y = exp * Math.sin(Math.PI * iny);
		
		Point retval = new Point(x, y);
		return retval;
	}
}

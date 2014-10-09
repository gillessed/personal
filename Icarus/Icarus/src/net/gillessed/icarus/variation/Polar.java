package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

public class Polar extends Variation {

	public Polar() {
		super();
	}
	@Override
	public Point calculate(double inx, double iny) {
		double x;
		double y;
		
		double theta = getTheta(inx, iny);
		double r = getR(inx,iny);
		
		x = theta / Math.PI;
		y = r - 1;
		
		Point retval = new Point(x, y);
		return retval;
	}
}

package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

public class Diamond extends Variation {

	public Diamond() {
		super();
	}
	@Override
	public Point calculate(double inx, double iny) {
		double x;
		double y;
		
		double r = getR(inx,iny);
		double theta = getTheta(inx,iny);
		
		x = Math.sin(theta) * Math.cos(r);
		y = Math.cos(theta) * Math.sin(r);
		
		Point retval = new Point(x, y);
		return retval;
	}
}

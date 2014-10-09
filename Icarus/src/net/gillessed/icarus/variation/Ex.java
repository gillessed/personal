package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

public class Ex extends Variation {

	public Ex() {
		super();
	}
	@Override
	public Point calculate(double inx, double iny) {
		double x;
		double y;
		
		double r = getR(inx,iny);
		double theta = getTheta(inx,iny);
		
		double p0 = Math.sin(theta + r);
		double p1 = Math.cos(theta - r);
		
		x = r *(Math.pow(p0, 3) + Math.pow(p1, 3));
		y = r *(Math.pow(p0, 3) - Math.pow(p1, 3));
		
		Point retval = new Point(x, y);
		return retval;
	}
}

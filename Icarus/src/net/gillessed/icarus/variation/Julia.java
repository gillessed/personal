package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

public class Julia extends Variation {

	public Julia() {
		super();
	}
	@Override
	public Point calculate(double inx, double iny) {
		double x;
		double y;
		
		double r = getR(inx, iny);
		double theta = getTheta(inx, iny);
		double omega = getOmega(inx,iny);
		
		x = Math.sqrt(r) * Math.cos(theta / 2 + omega);
		y = Math.sqrt(r) * Math.sin(theta / 2 + omega);
		
		Point retval = new Point(x, y);
		return retval;
	}
}

package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

public class Disc extends Variation {

	public Disc() {
		super();
	}
	@Override
	public Point calculate(double inx, double iny) {
		double x;
		double y;
		
		double theta = getTheta(inx, iny);
		double r = getR(inx, iny);
		
		x = theta/Math.PI*Math.sin(Math.PI*r);
		y = theta/Math.PI*Math.cos(Math.PI*r);
		
		Point retval = new Point(x, y);
		return retval;
	}

}

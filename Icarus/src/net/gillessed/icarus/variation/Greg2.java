package net.gillessed.icarus.variation;

import net.gillessed.icarus.geometry.Point;

public class Greg2 extends Variation {

	public Greg2() {
		super();
	}
	@Override
	public Point calculate(double inx, double iny) {
		double x;
		double y;
		
		double theta = getTheta(inx, iny);
		double random = getRandom(inx, iny);
		
		x = theta/Math.PI*Math.sin(Math.PI*(1+random)*inx);
		y = (1+theta)/(Math.PI-random)*Math.cos(Math.PI*(1+random)*iny);
		
		Point retval = new Point(x, y);
		return retval;
	}
}

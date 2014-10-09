package net.gillessed.icarus.geometry;

public class Translation implements Transformation {

	private double x;
	private double y;
	public Translation(double x, double y) {
		this.y = y;
		this.x = x;
	}
	
	@Override
	public Point transform(Point z) {
		return new Point(z.getX() + x, z.getY() + y);
	}

}

package net.gillessed.icarus.geometry;

public class CircleInversion implements Transformation {

	private final double radius;
	private final Point center;

	public CircleInversion(Point center, double radius) {
		this.center = center;
		this.radius = radius;
	}
	
	@Override
	public Point transform(Point z) {
		
		double dx = z.getX() - center.getX();
		double dy = z.getY() - center.getY();
		
		double x = center.getX() + radius * radius * dx / (dx * dx + dy * dy);
		double y = center.getY() + radius * radius * dy / (dx * dx + dy * dy);
		
		return new Point(x,y);
	}

}

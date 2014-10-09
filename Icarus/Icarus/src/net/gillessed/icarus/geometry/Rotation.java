package net.gillessed.icarus.geometry;

public class Rotation implements Transformation {
	private double theta;
	private boolean flipped;
	
	public Rotation(double theta, boolean flipped) {
		this.theta = theta;
		this.flipped = flipped;
	}
	
	public Point transform(Point z) {
		double x = z.getX();
		double y = z.getY();
		
		if(flipped) y = -y;
		
		double newX = x * Math.cos(theta) - y * Math.sin(theta);
		double newY = x * Math.sin(theta) + y * Math.cos(theta);
		return new Point(newX, newY);
	}
}

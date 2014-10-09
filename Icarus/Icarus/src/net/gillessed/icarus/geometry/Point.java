package net.gillessed.icarus.geometry;

public class Point {
	protected double x;
	protected double y;
	public Point() {
		x = y = 0;
	}
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}
	public Point(Point point){
		this.x = point.getX();
		this.y = point.getY();
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getX() {
		return x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getY() {
		return y;
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Point)) {
			return false;
		}
		Point point = (Point)obj;
		return (x == point.getX() && y == point.getY());
	}
	@Override
	public int hashCode() {
		int result = 17;
		result = result * 31 + new Double(x).hashCode();
		result = result * 31 + new Double(y).hashCode();
		return result;
	}
	
	@Override
	public String toString() {
		return "[" + x + "," + y + "]";
	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		double dx = x2 - x1;
		double dy = y2 - y1;
		return Math.sqrt(dx * dx + dy * dy);
	}
}

package net.gillessed.icarus.geometry;


public class ColorPoint extends Point {
	private int color;
	public ColorPoint() {
		super();
	}
	public ColorPoint(double x, double y) {
		super(x, y);
	}
	public ColorPoint(Point point, int color) {
		super(point);
		this.color = color;
	}
	public ColorPoint(ColorPoint point) {
		super(point.getX(), point.getY());
		color = point.getColor();
	}
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ColorPoint)) {
			return false;
		}
		ColorPoint point = (ColorPoint)obj;
		return (x == point.getX() && y == point.getY() && color == point.getColor());
	}
	@Override
	public int hashCode() {
		int result = 17;
		result = result * 31 + new Double(x).hashCode();
		result = result * 31 + new Double(y).hashCode();
		result = result * 31 + color * 17;
		return result;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getColor() {
		return color;
	}
}

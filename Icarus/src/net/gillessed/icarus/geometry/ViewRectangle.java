package net.gillessed.icarus.geometry;

import javax.swing.JPanel;

public class ViewRectangle {
	
	public static double DEFAULT_VALUE = -0.5;
	
	private double left;
	private double right;
	private double top;
	private double bottom;
	private final JPanel parent;
	public ViewRectangle(JPanel parent) {
		this.parent = parent;
		left = right = top = bottom = 0;
	}
	public ViewRectangle(double squareValue, double aspectRatio, JPanel parent) {
		this.parent = parent;
		resetBorders(squareValue, aspectRatio);
	}
	public ViewRectangle(double left, double right, double top, double bottom, JPanel parent) {
		this.parent = parent;
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}
	public void resetBorders(double squareValue, double aspectRatio) {
		left = -squareValue;
		right = - left;
		top = left * aspectRatio;
		bottom = -top;
	}
	public void setLeft(double left) {
		this.left = left;
	}
	public double getLeft() {
		return left;
	}
	public void setRight(double right) {
		this.right = right;
	}
	public double getRight() {
		return right;
	}
	public void setTop(double top) {
		this.top = top;
	}
	public double getTop() {
		return top;
	}
	public void setBottom(double bottom) {
		this.bottom = bottom;
	}
	public double getBottom() {
		return bottom;
	}
	public int changeX(double x)
	{
		return changeX(x, parent.getWidth());
	}
	public int changeX(double x, double width)
	{
		return (int)(x / (right - left) * width + width / 2);
	}
	public int changeY(double y)
	{
		return changeY(y, parent.getHeight());
	}
	public int changeY(double y, double height)
	{
		return (int)(y / (bottom - top) * height + height / 2);
	}
	public double reverseX(double x)
	{
		return ((double)(x) - parent.getWidth() / 2) * (right - left) / parent.getWidth();
	}
	public double reverseY(double y)
	{
		return ((double)(y) - parent.getHeight() / 2) * (bottom - top) / parent.getHeight();
	}
}

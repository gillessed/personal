package net.gillessed.icarus.geometry;

import java.util.ArrayList;
import java.util.List;

import net.gillessed.icarus.swingui.gradient.PathEditPanel;

public class TransformPath {
	private int triangleIndex;
	private int vertex;
	private final Point trianglePoint;
	private final List<Point> points;
	private final int rad = 7;
	
	public TransformPath(Point trianglePoint, int triangleIndex, int vertex) {
		this.trianglePoint = trianglePoint;
		this.triangleIndex = triangleIndex;
		this.vertex = vertex;
		points = new ArrayList<Point>();
	}
	
	public List<Point> getPoints() {
		return points;
	}

	public Point getTrianglePoint() {
		return trianglePoint;
	}
	
	public Point checkMouse(int mx, int my, PathEditPanel parent) {
		for(Point p : points) {
			if(Math.sqrt(Math.pow(parent.changeX(p.getX()) - mx,2) + Math.pow(parent.changeY(p.getY()) - my,2)) < getRad()) {
				return p;
			}
		}
		return null;
	}
	
	public Point checkMouseOnTPoint(int mx, int my, PathEditPanel parent) {
		if(Math.sqrt(Math.pow(parent.changeX(trianglePoint.getX()) - mx,2) + 
				Math.pow(parent.changeY(trianglePoint.getY()) - my,2)) < getRad()) {
			return trianglePoint;
		} else {
			return null;
		}
	}

	public int getRad() {
		return rad;
	}

	public int getTriangleIndex() {
		return triangleIndex;
	}

	public void setTriangleIndex(int triangleIndex) {
		this.triangleIndex = triangleIndex;
	}

	public int getVertex() {
		return vertex;
	}

	public void setVertex(int vertex) {
		this.vertex = vertex;
	}
	
	public Point getVectorForPercentage(double ratio) {
		double length = 0.0;
		double totalLength = getTotalLength();
		int index = -1;
		if(lengthOfSegment(0) < totalLength * ratio) {
			while(index < points.size() - 1) {
				index++;
				length += lengthOfSegment(index);
				if(length + lengthOfSegment(index + 1) > totalLength * ratio) {
					break;
				}
			}
		}
		Point currentPoint;
		if(index == -1) {
			currentPoint = trianglePoint;
		} else {
			currentPoint = points.get(index);
		}
		Point nextPoint;
		if(index + 1 < points.size()) {
			nextPoint = points.get(index + 1);
		} else {
			nextPoint = trianglePoint;
		}
		double neededLength = ratio * totalLength - length;
		Point vector = new Point(nextPoint.x - currentPoint.x, nextPoint.y - currentPoint.y);
		double vectorMagnitude = Point.distance(0, 0, vector.getX(), vector.getY());
		vector.setX(vector.getX() / vectorMagnitude * neededLength);
		vector.setY(vector.getY() / vectorMagnitude * neededLength);
		return new Point(currentPoint.getX() + vector.getX() - trianglePoint.getX(),
				currentPoint.getY() + vector.getY() - trianglePoint.getY());
	}
	
	public double lengthOfSegment(int index) {
		if(index == 0) {
			return Point.distance(trianglePoint.getX(), trianglePoint.getY(),
					points.get(0).getX(), points.get(0).getY());
		} else if(index == points.size()) {
			int last = points.size() - 1;
			return Point.distance(trianglePoint.getX(), trianglePoint.getY(),
					points.get(last).getX(), points.get(last).getY());
		} else {
			return Point.distance(points.get(index - 1).getX(), points.get(index - 1).getY(),
					points.get(index).getX(), points.get(index).getY());
		}
	}
	
	public double getTotalLength() {
		double sum = 0;
		for(int i = 0; i <= points.size(); i++) {
			sum += lengthOfSegment(i);
		}
		return sum;
	}
}

package com.gillessed.scanlater.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Selector {
	
	private boolean complete;
	private List<Point> points;
	
	public Selector() {
		complete = false;
		points = new ArrayList<Point>();
	}
	
	public void addPoint(Point point) {
		points.add(point);
	}
	
	public List<Point> getPoints() {
		return points;
	}
	
	public int size() {
		return points.size();
	}
	
	public boolean isEmpty() {
		return points.isEmpty();
	}
	
	public void mouseMoved(int mx, int my) {
		if(points.size() > 2) {
			double dx = mx - points.get(0).x;
			double dy = my - points.get(0).y;
			double distance = Math.sqrt(dx * dx + dy * dy);
			complete = distance < Globals.CIRCLE_RADIUS;
		} else {
			complete = false;
		}
	}
	
	public boolean isComplete() {
		return complete;
	}
	
	public void draw(Graphics2D g) {
		Point2D.Double mousePoint = Globals.instance().getMousePoint();
		int mx = (int)mousePoint.x;
		int my = (int)mousePoint.y;
		Stroke tempStroke = g.getStroke();
		if(complete) {
			g.setStroke(Globals.THICK_STROKE);
			g.setColor(new Color(0, 255, 0, 255));
		} else {
			g.setColor(new Color(0, 255, 0, 180));
		}
		if(points.size() == 1) {
			g.drawLine(mx, my, points.get(0).x, points.get(0).y);
		} else if(complete) {
			for(int i = 0; i < points.size() - 1; i++) {
				g.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y);
			}
			int l = points.size() - 1;
			g.drawLine(points.get(0).x, points.get(0).y, points.get(l).x, points.get(l).y);
		} else if(!points.isEmpty()) {
			for(int i = 0; i < points.size() - 1; i++) {
				g.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y);
			}
			int l = points.size() - 1;
			g.drawLine(mx, my, points.get(l).x, points.get(l).y);
			g.drawLine(mx, my, points.get(0).x, points.get(0).y);
		}
		g.setStroke(tempStroke);
	}
}

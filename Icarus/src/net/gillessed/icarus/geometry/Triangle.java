package net.gillessed.icarus.geometry;


import java.awt.Color;
import java.awt.Graphics2D;

import net.gillessed.icarus.AffineTransform;
import net.gillessed.icarus.swingui.transform.TransformShowPanel;

public class Triangle {
	private int rad;
	private Point a, b, c;
	private Color color;
	private final AffineTransform model;
	private boolean lastTouched;
	private final TransformShowPanel parent;
	
	public Triangle(AffineTransform model, TransformShowPanel parent) {
		this(model, parent, Color.gray);
	}
	
	public Triangle(AffineTransform model, TransformShowPanel parent, Color color) {
		a = new Point(0,0);
		b = new Point(1,0);
		c = new Point(0,1);
		this.model = model;
		if(model != null) {
			a.setX(model.getC());
			a.setY(model.getF());
			b.setX(model.getA() + model.getC());
			b.setY(model.getD() + model.getF());
			c.setX(model.getB() + model.getC());
			c.setY(model.getE() + model.getF());
		}
		this.parent = parent;
		rad = 5;
		this.color = color;
		setLastTouched(false);
	}
	
	public int checkMouse(int mx, int my) {
		int retVal = 0;
		if(Math.sqrt(Math.pow(parent.changeX(a.getX()) - mx,2) + Math.pow(parent.changeY(a.getY()) - my,2)) < rad)
		{
			retVal = 1;
		}
		else if(Math.sqrt(Math.pow(parent.changeX(b.getX()) - mx,2) + Math.pow(parent.changeY(b.getY()) - my,2)) < rad)
		{
			retVal = 2;
		}
		else if(Math.sqrt(Math.pow(parent.changeX(c.getX()) - mx,2) + Math.pow(parent.changeY(c.getY()) - my,2)) < rad)
		{
			retVal =  3;
		}
		return retVal;
	}
	
	public void draw(Graphics2D g, boolean drawData) {
		updateData();
		g.setColor(getColor());
		g.drawLine(parent.changeX(a.getX()), parent.changeY(a.getY()), parent.changeX(b.getX()), parent.changeY(b.getY()));
		g.drawLine(parent.changeX(c.getX()), parent.changeY(c.getY()), parent.changeX(b.getX()), parent.changeY(b.getY()));
		g.drawLine(parent.changeX(a.getX()), parent.changeY(a.getY()), parent.changeX(c.getX()), parent.changeY(c.getY()));
		
		g.drawOval(parent.changeX(a.getX()) - rad,parent.changeY(a.getY()) - rad, 2 * rad, 2 * rad);
		g.drawOval(parent.changeX(b.getX()) - rad,parent.changeY(b.getY()) - rad, 2 * rad, 2 * rad);
		g.drawOval(parent.changeX(c.getX()) - rad,parent.changeY(c.getY()) - rad, 2 * rad, 2 * rad);
		
		g.drawString("A",parent.changeX(a.getX()) - 10, parent.changeY(a.getY()) - 10);
		g.drawString("B",parent.changeX(b.getX()) - 10, parent.changeY(b.getY()) - 10);
		g.drawString("C",parent.changeX(c.getX()) - 15, parent.changeY(c.getY()) - 5);
		
		if(isLastTouched() && drawData) {
			drawData(g);
		}
	}
	
	private void updateData() {
		if(model != null) {
			model.setC(a.getX());
			model.setF(a.getY());
			model.setA(b.getX() - model.getC());
			model.setB(c.getX() - model.getC());
			model.setD(b.getY() - model.getF());
			model.setE(c.getY() - model.getF());
		}
	}
	
	public void drawData(Graphics2D g) {
		if(model != null) {
			g.setColor(getColor());
			g.drawString("A = " + model.getA(),10,20);
			g.drawString("B = " + model.getB(),10,40);
			g.drawString("C = " + model.getC(),10,60);
			g.drawString("D = " + model.getD(),10,80);
			g.drawString("E = " + model.getE(),10,100);
			g.drawString("F = " + model.getF(),10,120);
		}
	}
	
	public void setA(int x, int y) {
		a.setX(parent.reverseX(x));
		a.setY(parent.reverseY(y));
		normalize(a);
		updateData();
	}
	
	public void setB(int x, int y) {
		b.setX(parent.reverseX(x));
		b.setY(parent.reverseY(y));
		normalize(b);
		updateData();
	}
	
	public void setC(int x, int y) {
		c.setX(parent.reverseX(x));
		c.setY(parent.reverseY(y));
		normalize(c);
		updateData();
	}
	public void setA(double x, double y)
	{
		a.setX(x);
		a.setY(y);
		updateData();
	}
	
	public void setB(double x, double y) {
		b.setX(x);
		b.setY(y);
		updateData();
	}
	
	public void setC(double x, double y) {
		c.setX(x);
		c.setY(y);
		updateData();
	}
	
	public void normalize(Point p) {
		if(p.getX() > parent.getViewRectangle().getRight()) {
			p.setX(parent.getViewRectangle().getRight());
		}
		if(p.getX() < parent.getViewRectangle().getLeft()) {
			p.setX(parent.getViewRectangle().getLeft());
		}
		if(p.getY() < parent.getViewRectangle().getTop()) {
			p.setY(parent.getViewRectangle().getTop());
		}
		if(p.getY() > parent.getViewRectangle().getBottom()) {
			p.setY(parent.getViewRectangle().getBottom());
		}
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setLastTouched(boolean lastTouched) {
		this.lastTouched = lastTouched;
	}
	
	public boolean isLastTouched() {
		return lastTouched;
	}
	
	public Point getVertex(int i) {
		switch(i) {
		case 1: return a;
		case 2: return b;
		case 3: return c;
		default: return null;
		}
	}
	
	public void setVertex(Point p, int i) {
		switch(i) {
		case 1: setA(p.getX(), p.getY()); break;
		case 2: setB(p.getX(), p.getY()); break;
		case 3: setC(p.getX(), p.getY()); break;
		}
	}
}
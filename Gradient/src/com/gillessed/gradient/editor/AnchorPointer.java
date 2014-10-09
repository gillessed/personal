package com.gillessed.gradient.editor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * This is a UI component that is used to mark where color anchors
 * are in the gradient being edited.
 * 
 * @author Gregory Cole
 */

public class AnchorPointer {
	private static final int RADIUS = 8;
	private Color color;
	private final JPanel parent;
	private int x;
	private int y;
	private final int topheight;
	private boolean selected;
	
	public AnchorPointer(JPanel parent, Color color, int startx, int starty, int topheight) {
		this.parent = parent;
		this.color = color;
		this.x = startx;
		this.y = starty;
		this.topheight = topheight;
		this.selected = false;
	}
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x - RADIUS, y - RADIUS, 2 * RADIUS, 2 * RADIUS);
		g.drawLine(x, y, x, y + topheight);
		g.setColor(Color.black);
		Graphics2D g2 = (Graphics2D)g;
		if(selected) {
			g2.setStroke(new BasicStroke(4.0f));
		} else {
			g2.setStroke(new BasicStroke(1.0f));
		}
		g.drawRect(x - RADIUS, y - RADIUS, 2 * RADIUS, 2 * RADIUS);
		g2.setStroke(new BasicStroke(1.0f));
	}
	public void setX(int x) {
		this.x = x;
		parent.repaint();
	}
	public int getX() {
		return x;
	}
	public void setY(int y) {
		this.y = y;
		parent.repaint();
	}
	public int getY() {
		return y;
	}
	public boolean isSelected(int mx, int my) {
		return (x - RADIUS < mx && x + RADIUS > mx && y - RADIUS < my && y + RADIUS > my);
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public boolean isSelected() {
		return selected;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color newColor) {
		color = newColor;
	}
}

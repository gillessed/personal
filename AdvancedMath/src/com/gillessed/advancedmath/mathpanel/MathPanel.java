package com.gillessed.advancedmath.mathpanel;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public abstract class MathPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private double LEFT, RIGHT, TOP, BOTTOM;
	private boolean negativeUp = false;
	private boolean eraseBackground = false;
	private boolean drawAxes = false;
	
	public MathPanel() {
	}
	public void setViewBorder(double x0, double y0, double x1, double y1) {
		LEFT = x0;
		RIGHT = x1;
		if(negativeUp) {
			TOP = y1;
			BOTTOM = y0;
		} else {
			TOP = y0;
			BOTTOM = y1;
		}
	}
	public void setToNUnitSquare(double n) {
		setViewBorder(-n,-n,n,n);
	}
	public void setToBiUnitSquare() {
		setViewBorder(-1,-1,1,1);
	}
	public void setToUnitSquare() {
		setViewBorder(0,0,1,1);
	}
	public int convertXForScreen(double x) {
		return (int)((x - LEFT) / (RIGHT - LEFT) * getWidth());
	}
	public int convertYForScreen(double y) {
		int negativeMultiplier = negativeUp ? -1 : 1;
		return (int)((negativeUp ? 0 : getHeight()) - negativeMultiplier * (y - TOP) / (BOTTOM - TOP) * getHeight());
	}
	public double convertXFromScreen(int x) {
		return (double)x / (double)getWidth() * (RIGHT - LEFT) + LEFT;
	}
	public double convertYFromScreen(int y) {
		int negativeMultiplier = negativeUp ? -1 : 1;
		return (negativeUp ? 0 : BOTTOM) - negativeMultiplier * ((double)y / (BOTTOM - TOP) * (double)getHeight() + TOP);
	}
	public void setNegativeToTop(boolean b) {
		negativeUp = b;
	}
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(getBackground());
		if(eraseBackground)
			g.fillRect(0, 0, getWidth(), getHeight());
		if(drawAxes) {
			g.setColor(Color.black);
			int y = convertYForScreen(0);
			g.drawLine(0, y , getWidth(), y);
			int x = convertXForScreen(0);
			g.drawLine(x, 0, x, getHeight());
		}
		paintPanel(g);
	}
	protected abstract void paintPanel(Graphics g);
	public void setEraseBackground(boolean eraseBackground) {
		this.eraseBackground = eraseBackground;
	}
	public boolean isEraseBackground() {
		return eraseBackground;
	}
	public void eraseBackground(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0,0,getWidth(),getHeight());
	}
	public void setDrawAxes(boolean drawAxes) {
		this.drawAxes = drawAxes;
	}
	public boolean isDrawAxes() {
		return drawAxes;
	}
}

package net.gillessed.icarus.swingui.gradient;

import java.awt.Color;
import java.awt.Graphics;

import net.gillessed.icarus.Function;

import com.gillessed.gradient.Gradient;

/**
 * This class draws and performs events on the little balls above the gradient slider
 * in the gradient window.
 */
public class FunctionValuePointer {
	public static final int RADIUS = 7;
	private int value;
	private int x;
	private int y;
	private int xOffset;
	private Gradient gradient;
	private final Function f;
	public FunctionValuePointer(Function f, Gradient gradient) {
		this.f = f;
		this.gradient = gradient;
		value = f.getColor();
	}
	public void draw(Graphics g, int padding, int toppadding, int height) {
		if(gradient != null) {
			g.setColor(gradient.getColor(value));
		} else {
			g.setColor(Color.white);
		}
		x = value + padding;
		y = padding + RADIUS;
		g.drawLine(x, y, value + padding, height - padding);
		g.fillOval(value + padding - RADIUS, padding, 2 * RADIUS, 2 * RADIUS);
	}
	public boolean mouseOn(int mx, int my) {
		xOffset = x - mx;
		int dy = y - my;
		return Math.sqrt(xOffset * xOffset + dy * dy) < RADIUS;
	}
	public void setValue(int value) {
		int tempValue = value - xOffset;
		if(tempValue >= 0 && tempValue < Gradient.DEFAULT_SIZE) {
			this.value = tempValue;
		}
	}
	public void setGradient(Gradient gradient) {
		this.gradient = gradient;
	}
	public Gradient getGradient() {
		return gradient;
	}
	public void applyChanges() {
		f.setColor(value);
	}
}

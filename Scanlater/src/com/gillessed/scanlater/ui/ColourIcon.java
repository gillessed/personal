package com.gillessed.scanlater.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class ColourIcon implements Icon {
	
	private Color color;

	public ColourIcon(Color color) {
		this.color = color;
	}

	@Override
	public int getIconHeight() {
		return 20;
	}

	@Override
	public int getIconWidth() {
		return 30;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		int width = c.getWidth();
		int height = c.getHeight();
		g.setColor(color);
		g.fillRect(1, 1, width - 1, height - 1);
	}

	public Color getColour() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}

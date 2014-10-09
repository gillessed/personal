package com.gillessed.spacepenguin.gui.renderproperties;

import java.awt.Color;

public class ColorProperty {
	private Color color;
	public ColorProperty(String desc) {
		if(desc.charAt(0) == '#') {
			desc = desc.toLowerCase();
		} else {
			String[] values = desc.split(",");
			color = new Color(Integer.parseInt(values[0].trim()),
					Integer.parseInt(values[1].trim()),
					Integer.parseInt(values[2].trim()));
		}
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
}

package com.gillessed.spacepenguin.gui.renderproperties;

public class SPBorder {
	public enum Type {
		SOLID,
		DOTTED,
		DASHED,
	}
	public int thickness;
	public Type type;
	public SPColor color;
	
	public SPBorder(String[] desc) {
		thickness = Integer.parseInt(desc[0]);
		type = Type.valueOf(desc[1].toUpperCase());
		color = new SPColor(Integer.parseInt(desc[2]),
				Integer.parseInt(desc[3]),
				Integer.parseInt(desc[4]));
	}
	public SPBorder(int thickness, String type, SPColor color) {
		this.thickness = thickness;
		this.type = Type.valueOf(type);
		this.color = color;
	}
}

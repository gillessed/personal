package com.gillessed.spacepenguin.gui.renderproperties;


public class SPPositioning {
	public enum Layout {
		ABSOLUTE,
		RELATIVE_TL,
		RELATIVE_TR,
		RELATIVE_BL,
		RELATIVE_BR,
		FLOW,
		LEFT_X,
		MIDDLE_X,
		TOP_Y,
		MIDDLE_Y,
	}
	public Layout layout;
	public int x;
	public int y;
	public SPPositioning(String[] desc) {
		for(Layout l : Layout.values()) {
			if(l.toString().toLowerCase().equals(desc[0].toLowerCase())) {
				layout = l;
				break;
			}
		}
		if(layout == null) {
			throw new RuntimeException("Layout " + desc[0] + " not recongized.");
		}
		x = Integer.parseInt(desc[1]);
		y = Integer.parseInt(desc[2]);
	}
	public SPPositioning(Layout layout, int x, int y) {
		this.layout = layout;
		this.x = x;
		this.y = y;
	}
}
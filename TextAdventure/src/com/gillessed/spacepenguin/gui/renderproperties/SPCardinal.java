package com.gillessed.spacepenguin.gui.renderproperties;

public class SPCardinal {
	public int top;
	public int bottom;
	public int left;
	public int right;
	public SPCardinal(String[] desc) {
		left = Integer.parseInt(desc[0]);
		top = Integer.parseInt(desc[1]);
		right = Integer.parseInt(desc[2]);
		bottom = Integer.parseInt(desc[3]);
	}
	public SPCardinal(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
}

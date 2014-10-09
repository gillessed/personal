package com.gillessed.spacepenguin.gui.renderproperties;


public class SPDimension {
	public static final int WIDTH = -1;
	public static final int HEIGHT = -1;
	
	public int x;
	public int y;
	
	public SPDimension(String[] desc) {
		if(desc[0].toLowerCase().equals("width")) {
			x = WIDTH;
		} else {
			x = Integer.parseInt(desc[0]);
		}
		if(desc[1].toLowerCase().equals("height")){
			y = HEIGHT;
		} else {
			y = Integer.parseInt(desc[1]);
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}

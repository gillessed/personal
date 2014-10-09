package com.gillessed.spacepenguin.gui.renderproperties;

import java.awt.Point;

public class SPPoint extends Point {
	private static final long serialVersionUID = 4883867671389839944L;

	public SPPoint(String[] desc) {
		super(Integer.parseInt(desc[0]), Integer.parseInt(desc[1]));
	}
}

package com.gillessed.spacepenguin.gui.renderproperties;

import java.awt.Font;

public class SPFont extends Font {
	private static final long serialVersionUID = -7504025438266607073L;

	public SPFont(String[] desc) {
		super(desc[0], Font.PLAIN, Integer.parseInt(desc[1]));
	}
}

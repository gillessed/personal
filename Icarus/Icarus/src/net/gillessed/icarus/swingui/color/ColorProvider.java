package net.gillessed.icarus.swingui.color;

import java.awt.Color;

public interface ColorProvider {
	public Color getColor(int colorObject);
	public int getRandomColorObject();
}

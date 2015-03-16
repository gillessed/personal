package net.gillessed.icarus.swingui.gradient;

import java.awt.Color;
import java.io.Serializable;

public interface ColorProvider extends Serializable {
	public Color getColor(int colorObject);
	public int getRandomColorObject();
	public int getSize();
}

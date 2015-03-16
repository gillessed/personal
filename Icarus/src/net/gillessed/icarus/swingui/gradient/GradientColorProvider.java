package net.gillessed.icarus.swingui.gradient;

import java.awt.Color;

import com.gillessed.gradient.Gradient;

public class GradientColorProvider implements ColorProvider {
	private static final long serialVersionUID = 8427358303594869260L;
	
	private final Gradient gradient;
	
	public GradientColorProvider(Gradient gradient) {
		this.gradient = gradient;
	}
	
	@Override
	public Color getColor(int colorObject) {
		return gradient.getColor(colorObject);
	}
	
	@Override
	public int getRandomColorObject() {
		return 0;
	}

	@Override
	public int getSize() {
		return gradient.getSize();
	}
}

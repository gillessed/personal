package net.gillessed.icarus.swingui.color;

import java.awt.Color;

import com.gillessed.gradient.Gradient;

public class GradientColorProvider implements ColorProvider {

	private Gradient gradient;
	
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
	public void setGradient(Gradient gradient) {
		this.gradient = gradient;
	}
	public Gradient getGradient() {
		return gradient;
	}
}

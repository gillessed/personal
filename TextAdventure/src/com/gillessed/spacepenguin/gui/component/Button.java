package com.gillessed.spacepenguin.gui.component;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.gillessed.spacepenguin.gui.PressableRenderTarget;
import com.gillessed.spacepenguin.gui.renderproperties.RenderProperties;

public class Button extends PressableRenderTarget {
	
	protected String label;
	
	public Button(String id, String label) {
		super(id);
		this.label = label;
	}
	
	@Override
	public Rectangle draw(Graphics2D g) {
		super.draw(g);
		RenderProperties rp = getRenderProperties();
		g.setFont(rp.getFont(getState()));
		FontMetrics fm = g.getFontMetrics();
		g.setColor(rp.getBackgroundColor(getState()));
		g.fillRect(padding.x, padding.y, padding.width, padding.height);
		
		g.setColor(rp.getForegroundColor(getState()));
		g.drawString(label, inner.x + (inner.width - fm.stringWidth(label)) / 2, inner.y + inner.height - fm.getDescent());
		return margin;
	}

	@Override
	protected Dimension getInnerDimension(Graphics2D g) {
		FontMetrics fm = g.getFontMetrics();
		return new Dimension((int)fm.stringWidth(label), (int)fm.getHeight());
	}

	@Override
	public void setup() {}
}

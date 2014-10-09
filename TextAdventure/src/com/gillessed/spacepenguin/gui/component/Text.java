package com.gillessed.spacepenguin.gui.component;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.gillessed.spacepenguin.gui.Justification;
import com.gillessed.spacepenguin.gui.RenderTarget;
import com.gillessed.spacepenguin.gui.renderproperties.RenderProperties;

public class Text extends RenderTarget {
	
	protected String label;
	protected final Justification justification;
	
	public Text(String id, String label, Justification justification) {
		super(id);
		this.label = label;
		this.justification = justification;
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
		switch(justification) {
		case CENTER:
			g.drawString(label, inner.x + (inner.width - fm.stringWidth(label)) / 2, inner.y + inner.height - fm.getDescent());
			break;
		case LEFT:
			g.drawString(label, inner.x, inner.y + inner.height - fm.getDescent());
			break;
		case RIGHT:
			g.drawString(label, inner.x + inner.width - fm.stringWidth(label), inner.y + inner.height - fm.getDescent());
			break;
		default:
			break;
			
		}
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

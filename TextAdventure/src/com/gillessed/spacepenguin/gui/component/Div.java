package com.gillessed.spacepenguin.gui.component;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.gillessed.spacepenguin.gui.GraphicsUtils;
import com.gillessed.spacepenguin.gui.PressableRenderTarget;
import com.gillessed.spacepenguin.gui.RenderTarget;
import com.gillessed.spacepenguin.gui.renderproperties.RenderProperties;
import com.gillessed.spacepenguin.gui.renderproperties.SPDimension;

public class Div extends PressableRenderTarget {
	
	public Div(String id) {
		super(id);
	}
	
	@Override
	public Rectangle draw(Graphics2D g) {
		g.setColor(getRenderProperties().getBackgroundColor(state));
		g.fillRect(padding.x, padding.y, padding.width, padding.height);
		
		super.draw(g);
		lastDraw = null;
		for(RenderTarget child : children) {
			if(child.isVisible()) {
				lastDraw = child.draw(g);
			}
		}
		
		GraphicsUtils.setStrokeForBorder(g, getRenderProperties().getBorder(state));
		g.drawRect(padding.x, padding.y, padding.width - 1, padding.height - 1);
		
		return margin;
	}

	@Override
	protected Dimension getInnerDimension(Graphics2D g) {
		RenderProperties rp = getRenderProperties();
		SPDimension dim = rp.getDimensions(RenderState.ALL);
		if(dim == null) {
			return new Dimension();
		} else {
			return new Dimension(dim.x, dim.y);
		}
	}
}

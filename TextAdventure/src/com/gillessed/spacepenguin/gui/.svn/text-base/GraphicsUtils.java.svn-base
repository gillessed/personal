package com.gillessed.spacepenguin.gui;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

import com.gillessed.spacepenguin.gui.renderproperties.SPBorder;

public class GraphicsUtils {
	public static void setStrokeForBorder(Graphics2D g, SPBorder border) {
		switch(border.type) {
		case DASHED:
			g.setStroke(new BasicStroke(border.thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
					border.thickness, new float[] {border.thickness * 5}, 0f));
			break;
		case DOTTED:
			g.setStroke(new BasicStroke(border.thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
					1, new float[] {1}, 0f));
			break;
		case SOLID:
			g.setStroke(new BasicStroke(border.thickness));
			break;
		default:
			break;
		}
		g.setColor(border.color);
	}
}

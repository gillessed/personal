package com.gillessed.spacepenguin.gui.component;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.gillessed.spacepenguin.gui.renderproperties.RenderProperties;

public class TextArea extends Div {

	private StringBuilder textBuffer;
	
	public TextArea(String id) {
		super(id);
		textBuffer = new StringBuilder();
	}

	public void setText(String text) {
		textBuffer = new StringBuilder(text);
	}
	
	public void append(String text) {
		textBuffer.append(text);
	}
	
	public String getText() {
		return textBuffer.toString();
	}

	@Override
	public Rectangle draw(Graphics2D g) {
		Rectangle ret = super.draw(g);
		
		RenderProperties rp = getRenderProperties();
		g.setFont(rp.getFont(getState()));
		g.setColor(rp.getForegroundColor(getState()));
		FontMetrics fm = g.getFontMetrics();
		
		int fontHeight = fm.getHeight();
		
		int x = inner.x;
		int y = inner.y + fontHeight;
		
		StringBuilder buffer;
		for(int i = 0; i < textBuffer.length(); i++) {
			buffer = new StringBuilder();
			while(i < textBuffer.length() && !Character.isWhitespace(textBuffer.charAt(i))) {
				buffer.append(textBuffer.charAt(i));
				i++;
			}
			if(x + fm.stringWidth(buffer.toString()) > inner.x + inner.width) {
				x = inner.x;
				y += fontHeight;
				
			}
			g.drawString(buffer.toString(), x, y);
			x += fm.stringWidth(buffer.toString());
			if(i < textBuffer.length()) {
				if(textBuffer.charAt(i) == '\n') {
					x = inner.x;
					y += fontHeight;
				} else if(textBuffer.charAt(i) == ' ' && x != inner.x) {
					x += fm.stringWidth(" ");
				}
			}
		}
		
		return ret;
	}
}

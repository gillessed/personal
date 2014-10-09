package com.gillessed.scanlater.ui;

import com.gillessed.scanlater.Bubble;

public class Hover {
	private Bubble hoveredBubble;
	
	public Hover(Bubble hoveredBubble) {
		this.hoveredBubble = hoveredBubble;
		if(this.hoveredBubble != null) {
			this.hoveredBubble.setHovered(true);
		}
	}

	public Bubble getHoveredBubble() {
		return hoveredBubble;
	}

	public void finishHover() {
		if(this.hoveredBubble != null) {
			hoveredBubble.setHovered(false);
			hoveredBubble = null;
		}
	}
}

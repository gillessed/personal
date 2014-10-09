package com.gillessed.scanlater.ui.undo;

import com.gillessed.scanlater.Bubble;
import com.gillessed.scanlater.Project;
import com.gillessed.scanlater.ui.Globals;

public class FontStyleAction implements UndoAction {
	private int oldStyle;
	private int newStyle;
	private Bubble bubble;

	public FontStyleAction(Bubble bubble, int oldStyle, int newStyle) {
		this.bubble = bubble;
		this.oldStyle = oldStyle;
		this.newStyle = newStyle;
	}

	@Override
	public void undo(Project project) {
		Globals.instance().setSelectedBubble(bubble);
		Globals.instance().getSelectedBubble().setFontStyle(oldStyle);
		Globals.instance().selectedFontStyle = oldStyle;
	}

	@Override
	public void redo(Project project) {
		Globals.instance().setSelectedBubble(bubble);
		Globals.instance().getSelectedBubble().setFontStyle(newStyle);
		Globals.instance().selectedFontStyle = newStyle;
	}
}

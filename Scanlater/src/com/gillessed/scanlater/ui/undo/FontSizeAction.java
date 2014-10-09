package com.gillessed.scanlater.ui.undo;

import com.gillessed.scanlater.Bubble;
import com.gillessed.scanlater.Project;
import com.gillessed.scanlater.ui.Globals;

public class FontSizeAction implements UndoAction {
	private int oldSize;
	private int newSize;
	private Bubble bubble;

	public FontSizeAction(Bubble bubble, int oldSize, int newSize) {
		this.oldSize = oldSize;
		this.newSize = newSize;
		this.bubble = bubble;
	}

	@Override
	public void undo(Project project) {
		Globals.instance().setSelectedBubble(bubble);
		Globals.instance().getSelectedBubble().setFontSize(oldSize);
		Globals.instance().selectedFontSize = oldSize;
	}

	@Override
	public void redo(Project project) {
		Globals.instance().setSelectedBubble(bubble);
		Globals.instance().getSelectedBubble().setFontSize(newSize);
		Globals.instance().selectedFontSize = newSize;
	}
}

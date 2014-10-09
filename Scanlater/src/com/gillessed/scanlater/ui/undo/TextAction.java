package com.gillessed.scanlater.ui.undo;

import com.gillessed.scanlater.Bubble;
import com.gillessed.scanlater.Project;
import com.gillessed.scanlater.ui.Globals;

public class TextAction implements UndoAction {
	private Bubble bubble;
	private String oldText;
	private String newText;

	public TextAction(Bubble bubble, String oldText, String newText) {
		this.bubble = bubble;
		this.oldText = oldText;
		this.newText = newText;
	}

	@Override
	public void undo(Project project) {
		Globals.instance().setSelectedBubble(bubble);
		bubble.setTextString(oldText);
	}

	@Override
	public void redo(Project project) {
		Globals.instance().setSelectedBubble(bubble);
		bubble.setTextString(newText);
	}
}

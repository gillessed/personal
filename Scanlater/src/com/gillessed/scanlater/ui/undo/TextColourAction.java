package com.gillessed.scanlater.ui.undo;

import java.awt.Color;

import com.gillessed.scanlater.Bubble;
import com.gillessed.scanlater.Project;
import com.gillessed.scanlater.ui.Globals;

public class TextColourAction implements UndoAction {
	private Bubble bubble;
	private Color oldColour;
	private Color newColour;

	public TextColourAction(Bubble bubble, Color oldColour, Color newColour) {
		this.bubble = bubble;
		this.oldColour = oldColour;
		this.newColour = newColour;
	}

	@Override
	public void undo(Project project) {
		Globals.instance().setSelectedBubble(bubble);
		Globals.instance().getSelectedBubble().setTextColour(oldColour);
	}

	@Override
	public void redo(Project project) {
		Globals.instance().setSelectedBubble(bubble);
		Globals.instance().getSelectedBubble().setTextColour(newColour);
	}
}

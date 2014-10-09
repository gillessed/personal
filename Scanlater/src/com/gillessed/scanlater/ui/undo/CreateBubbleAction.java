package com.gillessed.scanlater.ui.undo;

import com.gillessed.scanlater.Bubble;
import com.gillessed.scanlater.Project;
import com.gillessed.scanlater.ui.Globals;

public class CreateBubbleAction implements UndoAction {
	
	private Bubble bubble;
	private int page;

	public CreateBubbleAction(Bubble bubble, int page) {
		this.bubble = bubble;
		this.page = page;
	}

	@Override
	public void undo(Project project) {
		project.getPage(page).removeBubble(bubble);
		Globals.instance().setSelectedBubble(null);
	}

	@Override
	public void redo(Project project) {
		project.getPage(page).addBubble(bubble);
		Globals.instance().setSelectedBubble(bubble);
	}
}

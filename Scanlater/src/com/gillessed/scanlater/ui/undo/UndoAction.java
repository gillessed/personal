package com.gillessed.scanlater.ui.undo;

import com.gillessed.scanlater.Project;

public interface UndoAction {
	public void undo(Project project);
	public void redo(Project project);
}

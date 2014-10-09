package com.gillessed.scanlater.ui.undo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.gillessed.scanlater.Project;

public class UndoStack {
	private final List<EditListener> editListeners;
	private final List<UndoListener> undoListeners;
	private final LinkedList<UndoAction> undoStack;
	private int index;
	
	public UndoStack() {
		editListeners = new ArrayList<>();
		undoListeners = new ArrayList<>();
		undoStack = new LinkedList<>();
		index = -1;
	}
	
	public void addAction(UndoAction action) {
		while(index < undoStack.size() - 1) {
			undoStack.removeLast();
		}
		undoStack.addLast(action);
		index++;
		for(EditListener el : editListeners) {
			el.editPerformed();
		}
		for(UndoListener ul : undoListeners) {
			ul.actionPerformed(undoStack.size(), index);
		}
	}
	
	public void undo(Project project) {
		if(index >= 0) {
			UndoAction action = undoStack.get(index);
			action.undo(project);
			index--;
			for(UndoListener ul : undoListeners) {
				ul.actionPerformed(undoStack.size(), index);
			}
		}
	}
	
	public void redo(Project project) {
		if(undoStack.size() > 0 && index < undoStack.size() - 1) {
			UndoAction action = undoStack.get(index + 1);
			action.redo(project);
			index++;
			for(UndoListener ul : undoListeners) {
				ul.actionPerformed(undoStack.size(), index);
			}
		}
	}
	
	public void addEditListener(EditListener editListener) {
		editListeners.add(editListener);
	}
	
	public void addUndoListener(UndoListener undoListener) {
		undoListeners.add(undoListener);
	}
}

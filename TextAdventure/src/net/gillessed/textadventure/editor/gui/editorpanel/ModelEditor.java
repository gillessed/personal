package net.gillessed.textadventure.editor.gui.editorpanel;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class ModelEditor<T> extends JPanel {
	public abstract void save();
	public abstract void delete();
	public abstract T getModel();
}

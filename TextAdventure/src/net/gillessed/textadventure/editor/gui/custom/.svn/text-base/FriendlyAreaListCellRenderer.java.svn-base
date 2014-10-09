package net.gillessed.textadventure.editor.gui.custom;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import net.gillessed.textadventure.datatype.FriendlyArea;

@SuppressWarnings("serial")
public class FriendlyAreaListCellRenderer extends DefaultListCellRenderer {

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected,cellHasFocus);
		if(value instanceof FriendlyArea) {
			FriendlyArea obj = (FriendlyArea)value;
			setText(obj.getName() + " (" + obj.getParent().getName() + ")");
		}
		return this;
	}

}

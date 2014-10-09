package net.gillessed.textadventure.editor.gui.custom;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import net.gillessed.textadventure.datatype.DataType;

@SuppressWarnings("serial")
public class DataTypeComboBoxCellRenderer extends DefaultListCellRenderer {
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected,cellHasFocus);
		DataType obj = (DataType)value;
		if(obj != null && obj.getParent() != null) {
			setText(obj.getName() + " (" + obj.getParent().getName() + ")");
		}
		return this;
	}
}

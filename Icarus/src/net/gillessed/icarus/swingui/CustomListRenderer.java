package net.gillessed.icarus.swingui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public abstract class CustomListRenderer<T> extends JLabel implements ListCellRenderer<T> {
	private static final long serialVersionUID = 4816585886599303404L;

	public CustomListRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends T> list, T value,
			int index, boolean isSelected, boolean cellHasFocus) {
		setText(getListString(value, index));
		Color background;
		Color foreground;
		if(isSelected) {
			background = Color.blue;
			foreground = Color.white;
		} else {
			background = Color.white;
			foreground = Color.black;
		}
		setBackground(background);
		setForeground(foreground);
		return this;
	}
	
	protected abstract String getListString(T value, int index);
}

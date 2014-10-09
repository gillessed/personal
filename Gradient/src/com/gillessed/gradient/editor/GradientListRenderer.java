package com.gillessed.gradient.editor;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.gillessed.gradient.Gradient;

/**
 * This renderer is used to draw the list in the gradient editor panel.
 * 
 * @author Gregory Cole
 *
 */

public class GradientListRenderer extends JLabel implements ListCellRenderer<Gradient> {
	private static final long serialVersionUID = 1L;


	public GradientListRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Gradient> list, Gradient value,
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
	
	protected String getListString(Gradient value,int index) {
		return value.toString();
	}
}

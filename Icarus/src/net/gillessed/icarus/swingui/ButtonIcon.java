package net.gillessed.icarus.swingui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;


/**
 * This class creates the icons on the buttons to edit the variation colors.
 */
class ButtonIcon implements Icon
{
	private Color color;
	public ButtonIcon(Color color)
	{
		this.setColor(color); 
	} 
	public int getIconWidth()
	{ 
		return 11;
	} 
	public int getIconHeight()
	{
		return 11;
	} 
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		g.setColor(Color.black); 
		g.fillRect(x, y, getIconWidth(), getIconHeight()); 
		
		g.setColor(color);
		
		g.fillRect(x+2, y+2, getIconWidth()-4, getIconHeight()-4);
 	}
	public void setColor(Color color) {
		this.color = color;
	}
	public Color getColor() {
		return color;
	}
}

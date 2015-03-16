package net.gillessed.icarus.swingui.gradient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.gillessed.gradient.Gradient;

public class GradientCellRenderer extends JLabel implements ListCellRenderer<Gradient> {

	private class GradientIcon implements Icon
	{
		private Gradient gradient;
		private final boolean selected;
		public GradientIcon(Gradient gradient, boolean selected)
		{
			this.gradient = gradient;
			this.selected = selected;
		} 
		public int getIconWidth()
		{ 
			return Gradient.DEFAULT_SIZE;
		} 
		public int getIconHeight()
		{
			return 30;
		} 
		public void paintIcon(Component c, Graphics g, int x, int y)
		{
			for(int i = 0; i < getIconWidth(); i++) {
				g.setColor(gradient.getColor(i % gradient.getSize()));
				g.drawLine(i, 0, i, getHeight());
			}
			if(selected) {
				g.setColor(Color.red); 
			} else {
				g.setColor(Color.black); 
			}
			g.drawRect(0, 0, getIconWidth(), getIconHeight() - 1); 
 		}
	}
	
	private static final long serialVersionUID = -526521619945794563L;

	public GradientCellRenderer() {
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
		setOpaque(true);
     }

     public Component getListCellRendererComponent(JList<? extends Gradient> list, Gradient value, int index, boolean isSelected, boolean cellHasFocus) {
    	 setIcon(new GradientIcon(value, isSelected));
    	 return this;
     }
}

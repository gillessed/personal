package net.gillessed.icarus.swingui.color;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import net.gillessed.icarus.FlameModel;

import com.gillessed.gradient.Gradient;

public class PredefinedGradientPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final JComboBox<Gradient> comboBox;

	public PredefinedGradientPanel(FlameModel model, List<Gradient> list) {
		Gradient[] gradients = new Gradient[list.size()];
		int i = 0;
		int selectedValue = -1;
		if(model.getColorProvider().getGradient() != null) {
			for(Gradient g : list) {
				gradients[i] = g;
				if(g.getName().equals(model.getColorProvider().getGradient().getName())) {
					selectedValue = i;
				}
				i++;
			}
		}
		comboBox = new JComboBox<Gradient>(gradients);
		GradientCellRenderer cellRenderer = new GradientCellRenderer();
		cellRenderer.setPreferredSize(new Dimension(Gradient.DEFAULT_SIZE, 30));
		comboBox.setRenderer(cellRenderer);
		if(selectedValue >= 0) {
			comboBox.setSelectedItem(gradients[selectedValue]);
		}
		add(getComboBox());
	}
	
	public Gradient getChosenGradient() {
		return (Gradient)getComboBox().getSelectedItem();
	}

	public JComboBox<Gradient> getComboBox() {
		return comboBox;
	}
}

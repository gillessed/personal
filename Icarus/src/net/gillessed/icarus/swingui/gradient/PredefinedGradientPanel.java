package net.gillessed.icarus.swingui.gradient;

import java.awt.Dimension;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import net.gillessed.icarus.FlameModel;
import net.gillessed.icarus.event.NewFlameListener;
import net.gillessed.icarus.swingui.FlameModelContainer;

import com.gillessed.gradient.Gradient;

public class PredefinedGradientPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	

	private final NewFlameListener flameChangeListener = new NewFlameListener() {
		@Override
		public void newFlame(FlameModel flameModel) {
			updateSelectedValue();
		}
	};

	private final FlameModelContainer flameModelContainer;
	private final JComboBox<ColorProvider> comboBox;
	private final List<ColorProvider> colorProviders;

	public PredefinedGradientPanel(FlameModelContainer flameModelContainer, List<ColorProvider> colorProviders) {
		this.flameModelContainer = flameModelContainer;
		this.flameModelContainer.addNewFlameListener(flameChangeListener);
		this.colorProviders = colorProviders;
		comboBox = new JComboBox<ColorProvider>(new Vector<ColorProvider>(colorProviders));
		ColorProviderCellRenderer cellRenderer = new ColorProviderCellRenderer();
		cellRenderer.setPreferredSize(new Dimension(Gradient.DEFAULT_SIZE, 30));
		comboBox.setRenderer(cellRenderer);
		add(comboBox);
	}
	
	private void updateSelectedValue() {
		int i = 0;
		int selectedValue = 0;
		for(ColorProvider g : colorProviders) {
			if(g.equals(flameModelContainer.getFlameModel().getColorProvider())) {
				selectedValue = i;
			}
			i++;
		}
		if(selectedValue >= 0) {
			comboBox.setSelectedItem(colorProviders.get(selectedValue));
		}
	}
	
	public ColorProvider getChosenGradient() {
		return (ColorProvider)comboBox.getSelectedItem();
	}
	
	public JComboBox<ColorProvider> getComboBox() {
		return comboBox;
	}
}
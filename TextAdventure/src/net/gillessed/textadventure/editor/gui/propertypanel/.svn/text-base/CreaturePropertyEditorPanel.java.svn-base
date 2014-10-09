package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import net.gillessed.textadventure.dataenums.CreatureProperties;
import net.gillessed.textadventure.utils.StringUtils;

@SuppressWarnings("serial")
public class CreaturePropertyEditorPanel extends PropertyPanel {
	private final List<JSpinner> creaturePropertySpinners;
	private final Map<CreatureProperties, Integer> creatureProperties;
	
	public CreaturePropertyEditorPanel(Map<CreatureProperties, Integer> creatureProperties, String title) {
		super((CreatureProperties.values().length + 1) / 2, 2);
		this.creatureProperties = creatureProperties;
		creaturePropertySpinners = new ArrayList<JSpinner>();
		for(int i = 0; i < CreatureProperties.values().length; i++) {
			int startValue = creatureProperties.get(CreatureProperties.values()[i]);
			SpinnerNumberModel spinnerModel = new SpinnerNumberModel(startValue, 0, 1000000, 1);
			JSpinner slider = new JSpinner(spinnerModel);
			String name = StringUtils.toCamelCase(CreatureProperties.values()[i].toString());
			creaturePropertySpinners.add(slider);
			addProperty(name, slider);
		}
		setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.black, 1), title));
	}
	
	public void save() {
		for(int i = 0; i < creaturePropertySpinners.size(); i++) {
			int value = (Integer)creaturePropertySpinners.get(i).getModel().getValue();
			creatureProperties.put(CreatureProperties.values()[i], value);
		}
	}
}

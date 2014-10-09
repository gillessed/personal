package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;

import net.gillessed.textadventure.dataenums.ItemProperties;
import net.gillessed.textadventure.utils.StringUtils;

@SuppressWarnings("serial")
public class ItemPropertyEditorPanel extends PropertyPanel {
	private final List<JSlider> itemPropertySliders;
	private final Map<ItemProperties, Integer> itemProperties;
	
	public ItemPropertyEditorPanel(Map<ItemProperties, Integer> itemProperties, String title) {
		super((ItemProperties.values().length + 1) / 2, 2);
		this.itemProperties = itemProperties;
		itemPropertySliders = new ArrayList<JSlider>();
		for(int i = 0; i < ItemProperties.values().length; i++) {
			JSlider slider = new JSlider(0, 1000, itemProperties.get(ItemProperties.values()[i]));
			String name = StringUtils.toCamelCase(ItemProperties.values()[i].toString());
			itemPropertySliders.add(slider);
			addProperty(name, slider);
		}
		setBorder(new TitledBorder(BorderFactory.createLineBorder(Color.black, 1), title));
	}
	
	public void save() {
		for(int i = 0; i < itemPropertySliders.size(); i++) {
			int value = itemPropertySliders.get(i).getValue();
			itemProperties.put(ItemProperties.values()[i], value);
		}
	}
}

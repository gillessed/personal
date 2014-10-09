package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.datatype.ItemBundle;
import net.gillessed.textadventure.datatype.ItemDescription;

@SuppressWarnings("serial")
public class ItemBundleCreatorPanel extends CreatorPanel<ItemBundle> {

	private final JSpinner itemAmountSpinner;
	private final JComboBox<ItemDescription> itemComboBox;
	
	public ItemBundleCreatorPanel(ItemBundle model, ItemBundleAdderPanel parent, String title) {
		super(model, parent);
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		itemAmountSpinner = new JSpinner(new SpinnerNumberModel(model.getItemAmount(), -Double.MAX_VALUE, Double.MAX_VALUE, 1));
		itemAmountSpinner.setPreferredSize(new Dimension(160, 20));
		add(new JLabel(title));
		add(itemAmountSpinner);
		add(Box.createHorizontalStrut(5));
		
		itemComboBox = new JComboBox<>(DataType.getAllOfType(ItemDescription.class).toArray(new ItemDescription[0]));
		if(model.getItem() != null) {
			itemComboBox.setSelectedItem(model.getItem());
		}
		add(new JLabel("Item: "));
		add(itemComboBox);
		add(Box.createHorizontalStrut(5));
		add(deleteButton);
	}

	@Override
	public void save() {
		model.setItemAmount(((SpinnerNumberModel)itemAmountSpinner.getModel()).getNumber().doubleValue());
		model.setItem((ItemDescription)itemComboBox.getSelectedItem());
	}

}

package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JComboBox;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.datatype.FriendlyArea;
import net.gillessed.textadventure.datatype.World;

@SuppressWarnings("serial")
public class FriendlyAreaChooserPanel extends CreatorPanel<FriendlyArea> {

	private final JComboBox<FriendlyArea> friendlyAreaComboBox;
	
	public FriendlyAreaChooserPanel(FriendlyArea model, FriendlyArea current, World world, final FriendlyAreaAdderPanel parent) {
		super(model, parent);
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		friendlyAreaComboBox = new JComboBox<FriendlyArea>(DataType.getAllFriendlyAreas(world, current).toArray(new FriendlyArea[0]));
		friendlyAreaComboBox.setSelectedItem(model);
		
		add(friendlyAreaComboBox);
		add(Box.createHorizontalStrut(5));
		add(deleteButton);
	}
	
	@Override
	public void save() {
		model = (FriendlyArea)friendlyAreaComboBox.getSelectedItem();
	}

	@Override
	public FriendlyArea getModel() {
		return model;
	}

}

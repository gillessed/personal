package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import net.gillessed.textadventure.datatype.AreaTransitionEventEffect;
import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.datatype.FriendlyArea;

@SuppressWarnings("serial")
public class AreaTransitionEventEffectCreatorPanel extends CreatorPanel<AreaTransitionEventEffect>{

	private JComboBox<FriendlyArea> friendlyAreaTransitionComboBox;
	
	public AreaTransitionEventEffectCreatorPanel(AreaTransitionEventEffect model, final EventEffectAdderPanel parent) {
		super(model, parent);
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		friendlyAreaTransitionComboBox = new JComboBox<>(DataType.getAllFriendlyAreas().toArray(new FriendlyArea[0]));
		if(model.getTarget() != null) {
			friendlyAreaTransitionComboBox.setSelectedItem(model.getTarget());
		}
		add(new JLabel("Area to Move to: "));
		add(friendlyAreaTransitionComboBox);
		add(Box.createHorizontalStrut(5));
		add(deleteButton);
	}

	@Override
	public void save() {
		model.setTarget((FriendlyArea)friendlyAreaTransitionComboBox.getSelectedItem());
	}

}

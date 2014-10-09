package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.datatype.EnemyDescription;
import net.gillessed.textadventure.datatype.EnemyEventEffect;

@SuppressWarnings("serial")
public class EnemyEventEffectCreatorPanel extends CreatorPanel<EnemyEventEffect> {
private JComboBox<EnemyDescription> enemyToFightComboBox;
	
	public EnemyEventEffectCreatorPanel(EnemyEventEffect model, final EventEffectAdderPanel parent) {
		super(model, parent);
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		enemyToFightComboBox = new JComboBox<>(DataType.getAllOfType(EnemyDescription.class).
				toArray(new EnemyDescription[0]));
		if(model.getTarget() != null) {
			enemyToFightComboBox.setSelectedItem(model.getTarget());
		}
		add(new JLabel("Enemy to fight: "));
		add(enemyToFightComboBox);
		add(Box.createHorizontalStrut(5));
		add(deleteButton);
	}

	@Override
	public void save() {
		model.setTarget((EnemyDescription)enemyToFightComboBox.getSelectedItem());
	}
}

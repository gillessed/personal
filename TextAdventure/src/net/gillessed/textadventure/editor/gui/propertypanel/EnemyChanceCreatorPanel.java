package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.datatype.EnemyChance;
import net.gillessed.textadventure.datatype.EnemyDescription;

@SuppressWarnings("serial")
public class EnemyChanceCreatorPanel extends CreatorPanel<EnemyChance> {

	private final JSpinner enemyChanceSpinner;
	private final JComboBox<EnemyDescription> itemComboBox;
	
	public EnemyChanceCreatorPanel(EnemyChance model, EnemyChanceAdderPanel parent, String title) {
		super(model, parent);
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		enemyChanceSpinner = new JSpinner(new SpinnerNumberModel(model.getEnemyAmount(), -Double.MAX_VALUE, Double.MAX_VALUE, 1));
		enemyChanceSpinner.setPreferredSize(new Dimension(160, 20));
		add(new JLabel(title));
		add(enemyChanceSpinner);
		add(Box.createHorizontalStrut(5));
		
		itemComboBox = new JComboBox<>(DataType.getAllOfType(EnemyDescription.class).toArray(new EnemyDescription[0]));
		if(model.getEnemy() != null) {
			itemComboBox.setSelectedItem(model.getEnemy());
		}
		add(new JLabel("Enemy: "));
		add(itemComboBox);
		add(Box.createHorizontalStrut(5));
		add(deleteButton);
	}

	@Override
	public void save() {
		model.setEnemyAmount(((SpinnerNumberModel)enemyChanceSpinner.getModel()).getNumber().doubleValue());
		model.setEnemy((EnemyDescription)itemComboBox.getSelectedItem());
	}

}

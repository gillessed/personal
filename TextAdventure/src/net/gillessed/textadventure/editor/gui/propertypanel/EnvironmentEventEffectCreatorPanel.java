package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import net.gillessed.textadventure.dataenums.EnvironmentEffectType;
import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.datatype.EnemyArea;
import net.gillessed.textadventure.datatype.EnvironmentEventEffect;
import net.gillessed.textadventure.datatype.FriendlyArea;
import net.gillessed.textadventure.datatype.Interaction;
import net.gillessed.textadventure.datatype.PlayableCharacter;
import net.gillessed.textadventure.datatype.Shop;
import net.gillessed.textadventure.editor.gui.custom.DataTypeComboBoxCellRenderer;
import net.gillessed.textadventure.editor.gui.custom.DataTypeTypeComboBoxCellRenderer;

@SuppressWarnings("serial")
public class EnvironmentEventEffectCreatorPanel extends CreatorPanel<EnvironmentEventEffect> {

	private final JComboBox<EnvironmentEffectType> effectTypeComboBox;
	private final JComboBox<Class<? extends DataType>> dataTypeTypeComboBox;
	private final DefaultComboBoxModel<Class<? extends DataType>> dataTypeTypeComboBoxModel;
	private final JComboBox<DataType> dataTypeComboBox;
	private final DefaultComboBoxModel<DataType> dataTypeComboBoxModel;

	@SuppressWarnings("unchecked")
	public EnvironmentEventEffectCreatorPanel(EnvironmentEventEffect model, final EventEffectAdderPanel parent) {
		super(model, parent);
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		effectTypeComboBox = new JComboBox<EnvironmentEffectType>(EnvironmentEffectType.values());
		effectTypeComboBox.setSelectedItem(model.getType());
		add(new JLabel("Effect Type: "));
		add(effectTypeComboBox);
		add(Box.createHorizontalStrut(5));
		
		Vector<Class<? extends DataType>> classTypes = new Vector<Class<? extends DataType>>();
		classTypes.add(Shop.class);
		classTypes.add(FriendlyArea.class);
		classTypes.add(EnemyArea.class);
		classTypes.add(Interaction.class);
		classTypes.add(PlayableCharacter.class);
		dataTypeTypeComboBoxModel = new DefaultComboBoxModel<Class<? extends DataType>>(classTypes);
		dataTypeTypeComboBox = new JComboBox<Class<? extends DataType>>(dataTypeTypeComboBoxModel);
		dataTypeTypeComboBox.setRenderer(new DataTypeTypeComboBoxCellRenderer());
		if(model.getTarget() == null) {
			dataTypeTypeComboBox.setSelectedItem(Shop.class);
		} else {
			dataTypeTypeComboBox.setSelectedItem(model.getTarget().getClass());
		}
		dataTypeTypeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dataTypeComboBoxModel.removeAllElements();
				Class<? extends DataType> clazz = (Class<? extends DataType>)dataTypeTypeComboBox.getSelectedItem();
				for(DataType type : DataType.getAllOfType(clazz)) {
					dataTypeComboBoxModel.addElement(type);
				}
			}
		});
		add(new JLabel("Data Type: "));
		add(dataTypeTypeComboBox);
		add(Box.createHorizontalStrut(5));
		
		Class<? extends DataType> clazz = (Class<? extends DataType>)dataTypeTypeComboBox.getSelectedItem();
		dataTypeComboBoxModel = new DefaultComboBoxModel<DataType>(DataType.getAllOfType(clazz).toArray(new DataType[0]));
		dataTypeComboBox = new JComboBox<DataType>(dataTypeComboBoxModel);
		dataTypeComboBox.setRenderer(new DataTypeComboBoxCellRenderer());
		if(model.getTarget() != null) {
			dataTypeComboBox.setSelectedItem(model.getTarget());
		}
		add(new JLabel("Target: "));
		add(dataTypeComboBox);
		add(Box.createHorizontalStrut(5));
		add(deleteButton);
	}
	
	@Override
	public void save() {
		model.setType((EnvironmentEffectType)effectTypeComboBox.getSelectedItem());
		model.setTarget((DataType)dataTypeComboBox.getSelectedItem());
	}

	@Override
	public void delete() {}
}

package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.datatype.EnemyArea;
import net.gillessed.textadventure.datatype.World;
import net.gillessed.textadventure.editor.gui.editorpanel.ModelEditor;
import net.gillessed.textadventure.utils.IconUtils;

@SuppressWarnings("serial")
public class EnemyAreaChooserPanel extends ModelEditor<EnemyArea> {

	private final JComboBox<EnemyArea> friendlyAreaComboBox;
	private final JButton deleteButton;
	private EnemyArea model;
	
	public EnemyAreaChooserPanel(EnemyArea model, EnemyArea current, World world, final EnemyAreaAdderPanel parent) {
		this.model = model;
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		friendlyAreaComboBox = new JComboBox<EnemyArea>(DataType.getAllEnemyAreas(world,
				current).toArray(new EnemyArea[0]));
		friendlyAreaComboBox.setSelectedItem(model);
		add(friendlyAreaComboBox);

		deleteButton = new JButton("Delete");
		deleteButton.setIcon(IconUtils.DELETE_ICON_16);
		deleteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.delete(EnemyAreaChooserPanel.this);
			}
		});
		add(Box.createHorizontalStrut(5));
		add(deleteButton);
	}
	
	@Override
	public void save() {
		model = (EnemyArea)friendlyAreaComboBox.getSelectedItem();
	}

	@Override
	public void delete() {}

	@Override
	public EnemyArea getModel() {
		return model;
	}

}

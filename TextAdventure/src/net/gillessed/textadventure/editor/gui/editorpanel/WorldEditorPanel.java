package net.gillessed.textadventure.editor.gui.editorpanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import net.gillessed.textadventure.datatype.EnemyArea;
import net.gillessed.textadventure.datatype.FriendlyArea;
import net.gillessed.textadventure.datatype.World;
import net.gillessed.textadventure.editor.gui.EditorFrame;
import net.gillessed.textadventure.utils.IconUtils;

@SuppressWarnings("serial")
public class WorldEditorPanel extends DataEditorPanel<World> {
	
	private final JButton newFriendlyAreaButton;
	private final JButton newEnemyAreaButton;
	
	public WorldEditorPanel(World model, EditorFrame editorFrame) {
		super(model, editorFrame, 3, 2);
		
		newFriendlyAreaButton = new JButton("New Friendly Area");
		newFriendlyAreaButton.setIcon(IconUtils.FRIENDLY_AREA_ICON_16);
		newFriendlyAreaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FriendlyArea newFriendlyArea = new FriendlyArea(WorldEditorPanel.this.model);
				WorldEditorPanel.this.model.
					addFriendlyArea(newFriendlyArea);
				WorldEditorPanel.this.editorFrame.
					newDataSelected(newFriendlyArea);
			}
		});
		addNewButton(newFriendlyAreaButton);
		
		newEnemyAreaButton = new JButton("New Enemy Area");
		newEnemyAreaButton.setIcon(IconUtils.ENEMY_AREA_ICON_16);
		newEnemyAreaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EnemyArea newEnemyArea = new EnemyArea(WorldEditorPanel.this.model);
				WorldEditorPanel.this.model.
					addEnemyArea(newEnemyArea);
				WorldEditorPanel.this.editorFrame.
					newDataSelected(newEnemyArea);
			}
		});
		addNewButton(newEnemyAreaButton);
		
		generateNameDescFields();
		generateSaveDeletePanel(true);
	}

	@Override
	public void save() {
		saveNameAndDescription();
	}

	@Override
	public void delete() {
		model.deleted();
	}
}

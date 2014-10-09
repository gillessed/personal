package net.gillessed.textadventure.editor.gui.editorpanel;

import net.gillessed.textadventure.datatype.EnemyArea;
import net.gillessed.textadventure.editor.gui.EditorFrame;
import net.gillessed.textadventure.editor.gui.propertypanel.EnemyChanceAdderPanel;
import net.gillessed.textadventure.editor.gui.propertypanel.GridCellEditorPanel;

@SuppressWarnings("serial")
public class EnemyAreaEditorPanel extends DataEditorPanel<EnemyArea> {

	private final EnemyChanceAdderPanel enemyChancePanel;
	private final GridCellEditorPanel gridPanel;
	
	public EnemyAreaEditorPanel(EnemyArea model, EditorFrame editorFrame) {
		super(model, editorFrame, 5, 0);
		
		generateNameDescFields();
		
		enemyChancePanel = new EnemyChanceAdderPanel(model.getEnemyEncounterChances(),
				this, "Enemy Encounter Chances", "Chance: ");
		getPropertyPanel().addSubPanel(enemyChancePanel);
		
		gridPanel = new GridCellEditorPanel(model.getGridCell(), model);
		getPropertyPanel().addSubPanel(gridPanel);
		
		generateSaveDeletePanel(true);
	}

	@Override
	public void save() {
		saveNameAndDescription();
		enemyChancePanel.save(true);
		gridPanel.save();
	}

	@Override
	public void delete() {
		model.deleted();
	}

}

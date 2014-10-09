package net.gillessed.textadventure.editor.gui.editorpanel;

import net.gillessed.textadventure.datatype.PlayableCharacter;
import net.gillessed.textadventure.editor.gui.EditorFrame;
import net.gillessed.textadventure.editor.gui.propertypanel.CreaturePropertyEditorPanel;

@SuppressWarnings("serial")
public class CharacterEditorPanel extends DataEditorPanel<PlayableCharacter> {

	private final CreaturePropertyEditorPanel creaturePropertyEditorPanel;
	
	public CharacterEditorPanel(PlayableCharacter model, EditorFrame editorFrame) {
		super(model, editorFrame, 4, 0);
		
		generateNameDescFields();
		
		creaturePropertyEditorPanel = new CreaturePropertyEditorPanel(model.getCreatureProperties(), 
				"Starting Stats");
		getPropertyPanel().addSubPanel(creaturePropertyEditorPanel);
		
		generateSaveDeletePanel(true);
	}

	@Override
	public void save() {
		saveNameAndDescription();
		creaturePropertyEditorPanel.save();
	}

	@Override
	public void delete() {
		model.deleted();
	}

}

package net.gillessed.textadventure.editor.gui.editorpanel;

import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.gillessed.textadventure.datatype.ItemDescription;
import net.gillessed.textadventure.editor.gui.EditorFrame;
import net.gillessed.textadventure.editor.gui.propertypanel.CreaturePropertyEditorPanel;
import net.gillessed.textadventure.editor.gui.propertypanel.ItemPropertyEditorPanel;
import net.gillessed.textadventure.editor.gui.propertypanel.StatusEffectAdderPanel;

@SuppressWarnings("serial")
public class ItemEditorPanel extends DataEditorPanel<ItemDescription> {
	
	private final JCheckBox rareCheckBox;
	private final SpinnerNumberModel valueSpinnerModel;
	private final JSpinner valueSpinner;
	private final ItemPropertyEditorPanel itemPropertyEditorPanel;
	private final CreaturePropertyEditorPanel creaturePropertyEditorPanel;
	private final StatusEffectAdderPanel effectEditorPanel;

	public ItemEditorPanel(ItemDescription model, EditorFrame editorFrame) {
		super(model, editorFrame, 8, 0);
		
		generateNameDescFields();
		
		rareCheckBox = new JCheckBox();
		rareCheckBox.setSelected(model.isRare());
		getPropertyPanel().addProperty("Is Rare: ", rareCheckBox);
		
		valueSpinnerModel = new SpinnerNumberModel(model.getValue(), 1L, Long.MAX_VALUE, 1L);
		valueSpinner = new JSpinner(valueSpinnerModel);
		getPropertyPanel().addProperty("Value: ", valueSpinner);
		
		itemPropertyEditorPanel = new ItemPropertyEditorPanel(model.getItemProperties(), "Item Properties");
		getPropertyPanel().addSubPanel(itemPropertyEditorPanel);

		creaturePropertyEditorPanel = new CreaturePropertyEditorPanel(model.getCreatureProperties(), "Creature Properties");
		getPropertyPanel().addSubPanel(creaturePropertyEditorPanel);
		
		effectEditorPanel = new StatusEffectAdderPanel(model.getEffects(), getPropertyPanel());
		getPropertyPanel().addSubPanel(effectEditorPanel);
		
		generateSaveDeletePanel(true);
	}

	@Override
	public void save() {
		saveNameAndDescription();
		model.setRare(rareCheckBox.isSelected());
		model.setValue(valueSpinnerModel.getNumber().longValue());
		itemPropertyEditorPanel.save();
		creaturePropertyEditorPanel.save();
		effectEditorPanel.save(true);
	}

	@Override
	public void delete() {
		model.deleted();
	}

}

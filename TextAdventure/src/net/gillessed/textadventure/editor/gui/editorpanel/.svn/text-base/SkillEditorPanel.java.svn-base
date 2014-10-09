package net.gillessed.textadventure.editor.gui.editorpanel;

import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.gillessed.textadventure.datatype.Skill;
import net.gillessed.textadventure.editor.gui.EditorFrame;
import net.gillessed.textadventure.editor.gui.propertypanel.AttackAdderPanel;
import net.gillessed.textadventure.editor.gui.propertypanel.CreaturePropertyEditorPanel;
import net.gillessed.textadventure.editor.gui.propertypanel.ItemPropertyEditorPanel;

@SuppressWarnings("serial")
public class SkillEditorPanel extends DataEditorPanel<Skill> {
	
	private final JSpinner hpCostSpinner;
	private final JSpinner mpCostSpinner;
	private final JSpinner levelSpinner;
	private final JCheckBox standardCheckBox;
	private final ItemPropertyEditorPanel itemPropertyEditorPanel;
	private final CreaturePropertyEditorPanel creaturePropertyEditorPanel;
	private final AttackAdderPanel attackAdderPanel;
	
	public SkillEditorPanel(Skill model, EditorFrame editorFrame) {
		super(model, editorFrame, 10, 2);
		generateNameDescFields();
		hpCostSpinner = new JSpinner(new SpinnerNumberModel(model.getHpCost(), 0, Long.MAX_VALUE, 1));
		getPropertyPanel().addProperty("HP Cost: ", hpCostSpinner);
		
		mpCostSpinner = new JSpinner(new SpinnerNumberModel(model.getMpCost(), 0, Long.MAX_VALUE, 1));
		getPropertyPanel().addProperty("MP Cost: ", mpCostSpinner);
		
		levelSpinner = new JSpinner(new SpinnerNumberModel(model.getLevel(), 0, Long.MAX_VALUE, 1));
		getPropertyPanel().addProperty("Level: ", levelSpinner);
		
		standardCheckBox = new JCheckBox();
		standardCheckBox.setSelected(model.isStandard());
		getPropertyPanel().addProperty("Is Standard: ", standardCheckBox);
		
		itemPropertyEditorPanel = new ItemPropertyEditorPanel(model.getItemProperties(), "Mininum Item Properties");
		getPropertyPanel().addSubPanel(itemPropertyEditorPanel);
		
		creaturePropertyEditorPanel = new CreaturePropertyEditorPanel(model.getCreatureProperties(), "Property Boosts");
		getPropertyPanel().addSubPanel(creaturePropertyEditorPanel);
		
		attackAdderPanel = new AttackAdderPanel(model.getAttacks(), model, this, "Attacks");
		getPropertyPanel().addSubPanel(attackAdderPanel);
		
		generateSaveDeletePanel(true);
	}

	@Override
	public void save() {
		saveNameAndDescription();
		model.setHpCost(((SpinnerNumberModel)hpCostSpinner.getModel()).getNumber().longValue());
		model.setMpCost(((SpinnerNumberModel)mpCostSpinner.getModel()).getNumber().longValue());
		model.setLevel(((SpinnerNumberModel)levelSpinner.getModel()).getNumber().longValue());
		model.setStandard(standardCheckBox.isSelected());
		itemPropertyEditorPanel.save();
		creaturePropertyEditorPanel.save();
		attackAdderPanel.save(true);
	}

	@Override
	public void delete() {
		model.deleted();
	}
}

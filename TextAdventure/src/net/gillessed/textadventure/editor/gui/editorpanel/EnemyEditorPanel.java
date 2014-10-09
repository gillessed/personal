package net.gillessed.textadventure.editor.gui.editorpanel;

import java.awt.Dimension;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.gillessed.textadventure.datatype.EnemyDescription;
import net.gillessed.textadventure.editor.gui.EditorFrame;
import net.gillessed.textadventure.editor.gui.propertypanel.CreaturePropertyEditorPanel;
import net.gillessed.textadventure.editor.gui.propertypanel.EventEffectAdderPanel;
import net.gillessed.textadventure.editor.gui.propertypanel.ItemBundleAdderPanel;

@SuppressWarnings("serial")
public class EnemyEditorPanel extends DataEditorPanel<EnemyDescription> {

	private final CreaturePropertyEditorPanel creaturePropertyEditorPanel;
	private final JSpinner goldDropSpinner;
	private final JSpinner expSpinner;
	private final ItemBundleAdderPanel itemDropsAdderPanel;
	private final EventEffectAdderPanel eventEffectAdderPanel;
	
	public EnemyEditorPanel(EnemyDescription model, EditorFrame editorFrame) {
		super(model, editorFrame, 8, 0);
		
		generateNameDescFields();
		
		creaturePropertyEditorPanel = new CreaturePropertyEditorPanel(model.getCreatureProperties(), "Enemy Stats");
		getPropertyPanel().addSubPanel(creaturePropertyEditorPanel);
		
		goldDropSpinner = new JSpinner(new SpinnerNumberModel(model.getGoldDrop(), Long.MIN_VALUE, Long.MAX_VALUE, 1));
		goldDropSpinner.setPreferredSize(new Dimension(160, 20));
		getPropertyPanel().addProperty("Gold drop: ", goldDropSpinner);
		
		expSpinner = new JSpinner(new SpinnerNumberModel(model.getExp(), Long.MIN_VALUE, Long.MAX_VALUE, 1));
		expSpinner.setPreferredSize(new Dimension(160, 20));
		getPropertyPanel().addProperty("Exp: ", expSpinner);
		
		itemDropsAdderPanel = new ItemBundleAdderPanel(model.getItemDrops(), this, "Item Drops", "Chance");
		getPropertyPanel().addSubPanel(itemDropsAdderPanel);
		
		eventEffectAdderPanel = new EventEffectAdderPanel(model.getEventEffects(), null, this);
		getPropertyPanel().addSubPanel(eventEffectAdderPanel);
		
		generateSaveDeletePanel(true);
	}

	@Override
	public void save() {
		saveNameAndDescription();
		model.setGoldDrop(((SpinnerNumberModel)goldDropSpinner.getModel()).getNumber().longValue());
		model.setExp(((SpinnerNumberModel)expSpinner.getModel()).getNumber().longValue());
		itemDropsAdderPanel.save(true);
		eventEffectAdderPanel.save(true);
	}

	@Override
	public void delete() {
		model.deleted();
	}
}

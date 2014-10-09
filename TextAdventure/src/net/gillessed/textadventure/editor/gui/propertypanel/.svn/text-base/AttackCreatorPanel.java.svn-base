package net.gillessed.textadventure.editor.gui.propertypanel;

import javax.swing.BoxLayout;

import net.gillessed.textadventure.datatype.Attack;

@SuppressWarnings("serial")
public class AttackCreatorPanel extends CreatorPanel<Attack> {

	private final StatusEffectAdderPanel statusEffectAdderPanel;
	
	public AttackCreatorPanel(Attack model, AttackAdderPanel parent) {
		super(model, parent);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		statusEffectAdderPanel = new StatusEffectAdderPanel(model.getEffects(), parent.getParentPanel());
		add(statusEffectAdderPanel);
		add(deleteButton);
	}

	@Override
	public void save() {
		statusEffectAdderPanel.save(true);
	}

}

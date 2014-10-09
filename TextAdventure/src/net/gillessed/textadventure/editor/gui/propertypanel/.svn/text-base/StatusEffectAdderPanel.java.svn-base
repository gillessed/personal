package net.gillessed.textadventure.editor.gui.propertypanel;

import java.util.List;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import net.gillessed.textadventure.datatype.StatusEffect;
import net.gillessed.textadventure.datatype.StatusAilmentEffect;

@SuppressWarnings("serial")
public class StatusEffectAdderPanel extends AdderPanel<StatusEffect, StatusEffectCreatorPanel<StatusEffect>> {

	private static final String STATUS_AILMENT_EFFECT = "Status Ailment Effect";
	private static final String MUTLIPLIER_EFFECT = "Multiplier Effect";
	
	private JComboBox<String> newEffectTypeComboBox;
	
	public StatusEffectAdderPanel(List<StatusEffect> effects, final JPanel parent) {
		super(effects, parent, "Effects");
		createGUI();
	}
	
	@Override
	protected void addToAddPanel() {
		String[] newEffectList = new String[] {STATUS_AILMENT_EFFECT, MUTLIPLIER_EFFECT};
		newEffectTypeComboBox = new JComboBox<String>(newEffectList);
		addPanel.add(newEffectTypeComboBox);
		addPanel.add(Box.createHorizontalStrut(5));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected StatusEffectCreatorPanel<StatusEffect> getCreatorPanel(StatusEffect e, int index) {
		StatusEffectCreatorPanel<? extends StatusEffect> newCreatorPanel = null;
		if(e instanceof StatusAilmentEffect) {
			newCreatorPanel = new StatusAilmentEffectCreatorPanel((StatusAilmentEffect)e, this);
		} 
		if(newCreatorPanel == null) {
			throw new RuntimeException("Effect Creator Panel for " + 
				e.getClass().getName() + " hasn't been implements yet.");
		}
		return (StatusEffectCreatorPanel<StatusEffect>)newCreatorPanel;
	}

	@Override
	protected StatusEffect generateNewElement(int index) {
		String str = (String)newEffectTypeComboBox.getSelectedItem();
		StatusEffect newEffect = null;
		switch(str) {
		case STATUS_AILMENT_EFFECT:
			newEffect = new StatusAilmentEffect();
			break;
		case MUTLIPLIER_EFFECT:
			break;
		}
		if(newEffect == null) {
			throw new RuntimeException("Uknown kind of effect! [ " + str + " ]");
		}
		return newEffect;
	}
}

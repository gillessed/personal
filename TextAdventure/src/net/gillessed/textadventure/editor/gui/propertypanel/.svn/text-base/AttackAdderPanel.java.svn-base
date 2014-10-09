package net.gillessed.textadventure.editor.gui.propertypanel;

import java.util.List;

import javax.swing.JPanel;

import net.gillessed.textadventure.datatype.Attack;
import net.gillessed.textadventure.datatype.Skill;

@SuppressWarnings("serial")
public class AttackAdderPanel extends AdderPanel<Attack, AttackCreatorPanel>{

	private final Skill parentSkill;

	public AttackAdderPanel(List<Attack> model, Skill parentSkill, JPanel parent, String title) {
		super(model, parent, title);
		this.parentSkill = parentSkill;
		createGUI();
	}

	@Override
	protected AttackCreatorPanel getCreatorPanel(Attack s, int index) {
		return new AttackCreatorPanel(s, this);
	}

	@Override
	protected Attack generateNewElement(int index) {
		return new Attack(parentSkill);
	}

}

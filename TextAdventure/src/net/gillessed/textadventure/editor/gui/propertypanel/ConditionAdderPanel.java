package net.gillessed.textadventure.editor.gui.propertypanel;

import java.util.List;

import javax.swing.JPanel;

import net.gillessed.textadventure.datatype.Condition;

@SuppressWarnings("serial")
public class ConditionAdderPanel extends AdderPanel<Condition, ConditionCreatorPanel> {

	public ConditionAdderPanel(List<Condition> model, JPanel parent, String title) {
		super(model, parent, title);
		createGUI();
	}

	@Override
	protected ConditionCreatorPanel getCreatorPanel(Condition s, int index) {
		return new ConditionCreatorPanel(s, this);
	}

	@Override
	protected Condition generateNewElement(int index) {
		return new Condition();
	}

}

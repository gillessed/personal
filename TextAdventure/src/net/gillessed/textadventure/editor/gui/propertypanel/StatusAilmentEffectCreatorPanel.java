package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.gillessed.textadventure.dataenums.StatusAilment;
import net.gillessed.textadventure.datatype.StatusAilmentEffect;

@SuppressWarnings("serial")
public class StatusAilmentEffectCreatorPanel extends StatusEffectCreatorPanel<StatusAilmentEffect> {
	private final JComboBox<StatusAilment> statusAilmentComboBox;
	private final JSpinner chanceSpinner;
	private final JSpinner turnsSpinner;

	public StatusAilmentEffectCreatorPanel(StatusAilmentEffect model, final StatusEffectAdderPanel effectEditorPanel) {
		super(model, effectEditorPanel);
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(new JLabel("Status ailment: "));
		statusAilmentComboBox = new JComboBox<StatusAilment>(StatusAilment.values());
		statusAilmentComboBox.setSelectedIndex(model.getStatusAilment().ordinal());
		add(statusAilmentComboBox);
		add(new JLabel(" Chance: "));
		chanceSpinner = new JSpinner(new SpinnerNumberModel(model.getChance(), 0, 1, 0.01));
		add(chanceSpinner);
		add(new JLabel(" Turns: "));
		turnsSpinner = new JSpinner(new SpinnerNumberModel(model.getTurns(), 0, 100, 1));
		add(turnsSpinner);
		add(Box.createHorizontalStrut(5));
		add(deleteButton);
	}
	
	public void save() {
		model.setStatusAilment((StatusAilment)statusAilmentComboBox.getSelectedItem());
		model.setChance((Double)chanceSpinner.getModel().getValue());
		model.setTurns((Integer)turnsSpinner.getModel().getValue());
	}

	@Override
	public void delete() {}
}

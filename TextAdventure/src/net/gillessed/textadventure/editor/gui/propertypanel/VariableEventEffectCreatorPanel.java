package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.datatype.Variable;
import net.gillessed.textadventure.datatype.VariableEventEffect;

@SuppressWarnings("serial")
public class VariableEventEffectCreatorPanel extends CreatorPanel<VariableEventEffect> {

	private final JComboBox<Variable> variableComboBox;
	private final JTextField expressionField;
	
	public VariableEventEffectCreatorPanel(VariableEventEffect model, EventEffectAdderPanel parent) {
		super(model, parent);
	setLayout(new FlowLayout(FlowLayout.LEFT));
		
		variableComboBox = new JComboBox<>(DataType.getAllOfType(Variable.class).toArray(new Variable[0]));
		if(model.getTarget() != null) {
			variableComboBox.setSelectedItem(model.getTarget());
		}
		add(new JLabel("Variable: "));
		add(variableComboBox);
		
		expressionField = new JTextField(25);
		expressionField.setText(model.getExpression());
		add(Box.createHorizontalStrut(5));
		add(new JLabel("Expression: "));
		add(expressionField);
		add(deleteButton);
	}

	@Override
	public void save() {
		model.setTarget((Variable)variableComboBox.getSelectedItem());
		model.setExpression(expressionField.getText());
	}

}

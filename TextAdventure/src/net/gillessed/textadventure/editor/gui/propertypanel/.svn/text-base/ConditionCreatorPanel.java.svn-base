package net.gillessed.textadventure.editor.gui.propertypanel;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.gillessed.textadventure.dataenums.Operator;
import net.gillessed.textadventure.datatype.Condition;
import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.datatype.Variable;

@SuppressWarnings("serial")
public class ConditionCreatorPanel extends CreatorPanel<Condition>{

	private final JComboBox<Variable> variableComboBox;
	private final JComboBox<Operator> operatorComboBox;
	private final JSpinner valueSpinner;
	
	public ConditionCreatorPanel(Condition model, ConditionAdderPanel parent) {
		super(model, parent);
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		add(new JLabel("Condition:"));
		add(Box.createHorizontalStrut(5));
		
		variableComboBox = new JComboBox<>(DataType.getAllOfType(Variable.class).toArray(new Variable[0]));
		if(model.getVariable() != null) {
			variableComboBox.setSelectedItem(model.getVariable());
		}
		add(variableComboBox);
		
		operatorComboBox = new JComboBox<>(Operator.values());
		operatorComboBox.setSelectedItem(model.getOperator());
		add(operatorComboBox);
		
		valueSpinner = new JSpinner(new SpinnerNumberModel(model.getValue(), -Double.MAX_VALUE, Double.MAX_VALUE, 1));
		valueSpinner.setPreferredSize(new Dimension(160, 20));
		add(valueSpinner);
		
		add(Box.createHorizontalStrut(5));
		add(deleteButton);
	}

	@Override
	public void save() {
		model.setVariable((Variable)variableComboBox.getSelectedItem());
		model.setOperator((Operator)operatorComboBox.getSelectedItem());
		model.setValue(((SpinnerNumberModel)valueSpinner.getModel()).getNumber().doubleValue());
	}

}

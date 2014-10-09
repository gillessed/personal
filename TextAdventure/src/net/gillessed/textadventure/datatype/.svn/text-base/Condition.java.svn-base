package net.gillessed.textadventure.datatype;

import javax.swing.Icon;

import net.gillessed.textadventure.dataenums.Operator;
import net.gillessed.textadventure.deletelistener.DeleteListener;

@SuppressWarnings("serial")
public class Condition extends DataType {

	private Variable variable;
	private final DeleteListener variableListener;
	private Operator operator;
	private double value;
	
	public Condition() {
		super(null);
		value = 0;
		operator = Operator.values()[0];
		variableListener = new DeleteListener() {
			private static final long serialVersionUID = 3238345671040744833L;
			
			@Override
			public void deleted(DataType deleted) {
				setVariable(null);
			}
		};
	}

	public Variable getVariable() {
		return variable;
	}

	public void setVariable(Variable variable) {
		if(this.variable != null) {
			this.variable.removeDeleteListener(variableListener);
		}
		this.variable = variable;
		if(variable != null) {
			variable.addDeleteListener(variableListener);
		}
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public Icon getIcon(int size) {
		return null;
	}
}

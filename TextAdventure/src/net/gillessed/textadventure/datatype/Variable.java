package net.gillessed.textadventure.datatype;

import javax.swing.Icon;

import net.gillessed.textadventure.utils.IconUtils;

public class Variable extends DataType {
	private static final long serialVersionUID = -8912221952526879113L;

	private double value;
	
	public Variable(Universe parent) {
		super(parent);
	}
	
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public Icon getIcon(int size) {
		return IconUtils.VARIABLE_ICON(size);
	}

}

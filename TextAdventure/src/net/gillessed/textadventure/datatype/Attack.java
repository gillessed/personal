package net.gillessed.textadventure.datatype;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;

public class Attack extends DataType {

	private static final long serialVersionUID = -2155467078135523661L;

	private final List<StatusEffect> effects;
	
	public Attack(DataType parent) {
		super(parent);
		effects = new ArrayList<StatusEffect>();
	}

	public List<StatusEffect> getEffects() {
		return effects;
	}

	@Override
	public Icon getIcon(int size) {
		return null;
	}
}

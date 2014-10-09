package net.gillessed.textadventure.dataenums;

import net.gillessed.textadventure.utils.StringUtils;

public enum Operator {
	EQUALS,
	NOT_EQUALS,
	LESS_THAN,
	GREATER_THAN;
	
	public String toString() {
		return StringUtils.toCamelCase(super.toString());
	};
}

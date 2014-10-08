package com.gillessed.magic.state;

public class ModularInt {
	private int modulo;
	private int value;

	public ModularInt(int modulo) {
		this.modulo = modulo;
		value = 0;
		normalize();
	}
	
	public ModularInt(int value, int modulo) {
		this.modulo = modulo;
		this.value = value;
		normalize();
	}
	
	private void normalize() {
		while(value < 0) {
			value += modulo;
		}
		value %= modulo;
	}
	
	public void add(int n) {
		value += n;
		normalize();
	}
	
	public ModularInt addConstructive(int n) {
		return new ModularInt(value + n, modulo);
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result += modulo * 31;
		result += value * 31;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ModularInt)) {
			return false;
		}
		ModularInt other = (ModularInt)obj;
		return other.getValue() == value;
	}

	public ModularInt copy() {
		return new ModularInt(value, modulo);
	}
}

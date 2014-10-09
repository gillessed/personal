package com.gillessed.gscript;

public class GObject {
	private final Object value;
	private final String type;

	public GObject(Object value, String type) {
		this.value = value;
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public String getType() {
		return type;
	}
}

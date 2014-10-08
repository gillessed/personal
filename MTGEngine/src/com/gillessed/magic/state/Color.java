package com.gillessed.magic.state;

import java.util.ArrayList;
import java.util.List;

public class Color {
	public enum Value {
		WHITE,
		GREEN,
		RED,
		BLUE,
		BLACK
	};
	
	private List<Value> values;
	
	public Color(Value... values) {
		this.values = new ArrayList<Value>();
		setColor(values);
	}
	
	public boolean isColor(Value value) {
		if(value == null) {
			return values.isEmpty();
		} else {
			return values.contains(value);
		}
	}
	
	public void setColor(Value... values) {
		this.values.clear();
		for(Value v : values) {
			this.values.add(v);
		}
	}
}

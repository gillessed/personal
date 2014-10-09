package com.gillessed.gscript;

import java.util.HashMap;
import java.util.Map;

public class Environment {
	
	private final Map<String, GObject> objectMap;
	
	public Environment() {
		objectMap = new HashMap<>();
	}
	
	public GObject getValueForIdentifier(String identifier) {
		if(!objectMap.containsKey(identifier)) {
			throw new RuntimeException("Variable " + identifier + " not set.");
		}
		return objectMap.get(identifier);
	}
	
	public void setValueForIdentifier(String identifier, GObject value) {
		objectMap.put(identifier, value);
	}
}

package com.gillessed.gscript;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.gillessed.gscript.GObject.Type;
import com.gillessed.gscript.ast.ASTFunction;

public class Environment {

    private final Set<String> unmodifiableValues;
    private final Map<String, String> importMap;
	private final Map<String, GObject> objectMap;
	private final Map<FunctionKey, GObject> functionMap; 
	private final Environment parent;
	
	public Environment() {
	    parent = null;
	    importMap = new HashMap<>();
        objectMap = new HashMap<>();
        functionMap = new HashMap<>();
		unmodifiableValues = new HashSet<>();
	}
	
    public Environment(Environment parent) {
        this.parent = parent;
        importMap = new HashMap<>();
        objectMap = new HashMap<>();
        functionMap = new HashMap<>();
        unmodifiableValues = new HashSet<>();
    }
    
    public Map<String, String> getImportMap() {
        return importMap;
    }
    
    public Map<String, GObject> getObjectMap() {
        return objectMap;
    }
    
    public Map<FunctionKey, GObject> getFunctionMap() {
        return functionMap;
    }
    
    public GObject getFunctionForKey(FunctionKey key) throws GScriptException {
        Environment current = this;
        while(current != null) {
            if(!current.getFunctionMap().containsKey(key)) {
                current = current.getParent();
            } else {
                return current.getFunctionMap().get(key);
            }
        }
        throw new GScriptException("Function " + key + " does not exist.");
    }
    
	public GObject getValueForIdentifier(String identifier) throws GScriptException {
	    Environment current = this;
	    while(current != null) {
	        if(current.getObjectMap().containsKey(identifier)) {
	            return current.getObjectMap().get(identifier);
	        }
            current = current.getParent();
	    }
		throw new GScriptException("Variable " + identifier + " not set.");
	}

    public void addImport(String identifier, String className) throws GScriptException {
	       if(unmodifiableValues.contains(identifier)) {
	            throw new GScriptException("Cannot import class for name: " + identifier + " because it is already set.");
	        }
	        importMap.put(identifier, className);
	}
    
    public Environment getContainingEnv(FunctionKey key) {
        Environment current = this;
        while(current != null) {
            if(current.getFunctionMap().containsKey(key)) {
                return current;
            }
            current = current.getParent();
        }
        return this;
    }
    
    public Environment getContainingEnv(String identifier) {
        Environment current = this;
        while(current != null) {
            if(current.getObjectMap().containsKey(identifier)) {
                return current;
            }
            current = current.getParent();
        }
        return this;
    }
	
	public void setValueForIdentifier(String identifier, GObject value) throws GScriptException {
	    if(unmodifiableValues.contains(identifier)) {
	        throw new GScriptException("Cannot set value for name: " + identifier + " because it is already set.");
	    }
        addObject(identifier, value);
	}
    
    public void setUnmodifiableValueForIdentifier(String identifier, GObject value) throws GScriptException {
        if(unmodifiableValues.contains(identifier)) {
            throw new GScriptException("Cannot set value for name: " + identifier + " because it is already set.");
        }
        unmodifiableValues.add(identifier);
        addObject(identifier, value);
    }
    
    private void addObject(String identifier, GObject value) {
        if(value.getType() == Type.FUNCTION) {
            ASTFunction function = (ASTFunction)value.getValue();
            functionMap.put(function.getKey(), value);
        } else {
            Environment env = getContainingEnv(identifier);
            env.getObjectMap().put(identifier, value);
        }
    }
    
	public Environment push() {
	    Environment child = new Environment(this);
	    return child;
	}
    
    public Environment getParent() {
        return parent;
    }
}

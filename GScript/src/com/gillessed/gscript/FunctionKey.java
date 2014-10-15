package com.gillessed.gscript;

public class FunctionKey {
    private final String name;
    private final int arguments;

    public FunctionKey(String name, int arguments) {
        this.name = name;
        this.arguments = arguments;
    }
    
    @Override
    public int hashCode() {
        int result = 17;
        result += name.hashCode() * 31;
        result += arguments * 31;
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof FunctionKey)) {
            return false;
        }
        FunctionKey key = (FunctionKey)obj;
        return (this.name.equals(key.getName()) && this.arguments == key.getArguments());
    }

    public String getName() {
        return name;
    }

    public int getArguments() {
        return arguments;
    }
    
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(name);
        ret.append("(");
        if(arguments > 0) {
            ret.append("1");
            for(int i = 1; i < arguments; i++) {
                ret.append(",");
                ret.append(i);
            }
        }
        ret.append(")");
        return ret.toString();
    }
}

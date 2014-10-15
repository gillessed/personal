package com.gillessed.gscript;

public class GScriptException extends Exception {
    private static final long serialVersionUID = -8941768965895918017L;
    
    private final String message;
    
    public GScriptException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

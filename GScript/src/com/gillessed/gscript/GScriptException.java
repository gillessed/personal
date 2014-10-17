package com.gillessed.gscript;

public class GScriptException extends Exception {
    private static final long serialVersionUID = -8941768965895918017L;
    
    private final String message;
    private int lineNumber;
    
    public GScriptException(String message, int lineNumber) {
        this.message = message;
        this.lineNumber = lineNumber;
    }

    public String getMessage() {
        return "[" + message + "] at line " + lineNumber + ".";
    }
}

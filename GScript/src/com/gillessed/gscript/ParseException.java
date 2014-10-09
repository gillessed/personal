package com.gillessed.gscript;

public class ParseException extends Exception {
	private static final long serialVersionUID = -2613297959514067178L;
	
	private final String message;
	
	public ParseException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}

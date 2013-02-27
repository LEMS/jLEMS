package org.lemsml.jlems.core.sim;


public class ParseException extends Exception {
	private static final long serialVersionUID = 1L;

	String message;

	public ParseException(String msg) {
		super(msg);
		message = msg;
	}

	public String toString() {
		return "ParseException: " + message;
	}

}

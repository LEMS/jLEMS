package org.lemsml.jlems.core.expression;

public class ParseError extends Exception {
	private static final long serialVersionUID = 1L;


	public ParseError(String msg) {
		super(msg);
	}
	public ParseError(String msg, Throwable t) {
		super(msg, t);
	}
	
}

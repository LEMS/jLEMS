package org.lemsml.jlems.core.expression;

import org.lemsml.jlems.core.sim.LEMSException;

public class ParseError extends LEMSException {
	private static final long serialVersionUID = 1L;


	public ParseError(String msg) {
		super(msg);
	}
	public ParseError(String msg, Throwable t) {
		super(msg, t);
	}
	
}

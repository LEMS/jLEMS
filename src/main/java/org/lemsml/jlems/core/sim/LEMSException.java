package org.lemsml.jlems.core.sim;

public class LEMSException extends Exception {
	private static final long serialVersionUID = 1L;

	public LEMSException(String msg) {
		super(msg);
	}
	public LEMSException(String msg, Throwable t) {
		super(msg, t);
	}
    
	public LEMSException(Throwable t) {
		super(t);
	}
}

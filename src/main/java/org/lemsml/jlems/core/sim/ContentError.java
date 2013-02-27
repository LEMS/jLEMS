package org.lemsml.jlems.core.sim;

public class ContentError extends Exception {
	private static final long serialVersionUID = 1L;


	public ContentError(String msg) {
		super(msg);
	}
	public ContentError(String msg, Throwable t) {
		super(msg, t);
	}
	
}

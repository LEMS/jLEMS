package org.lemsml.jlems.core.run;

public class RuntimeError extends Exception {
	private static final long serialVersionUID = 1L;


	public RuntimeError(String msg) {
		super(msg);
	}
	public RuntimeError(String msg, Throwable t) {
		super(msg, t);
	}
	
}

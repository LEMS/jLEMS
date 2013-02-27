package org.lemsml.jlems.core.codger;

public class CompilationException extends Exception {
	private static final long serialVersionUID = 1L;


	public CompilationException(String msg) {
		super(msg);
	}
	public CompilationException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public CompilationException(Throwable t) {
		super(t);
	}
}

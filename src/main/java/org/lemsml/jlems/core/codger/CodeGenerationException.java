package org.lemsml.jlems.core.codger;

public class CodeGenerationException extends Exception {
	private static final long serialVersionUID = 1L;


	public CodeGenerationException(String msg) {
		super(msg);
	}
	public CodeGenerationException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public CodeGenerationException(Throwable t) {
		super(t);
	}
}

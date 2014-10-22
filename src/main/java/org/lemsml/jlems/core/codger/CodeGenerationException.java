package org.lemsml.jlems.core.codger;

import org.lemsml.jlems.core.sim.LEMSException;

public class CodeGenerationException extends LEMSException {
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

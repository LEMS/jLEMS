package org.lemsml.jlems.core.run;

import org.lemsml.jlems.core.sim.LEMSException;

public class RuntimeError extends LEMSException {
	private static final long serialVersionUID = 1L;


	public RuntimeError(String msg) {
		super(msg);
	}
	public RuntimeError(String msg, Throwable t) {
		super(msg, t);
	}
	
}

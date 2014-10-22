package org.lemsml.jlems.core.run;

import org.lemsml.jlems.core.sim.LEMSException;

public class ConnectionError extends LEMSException {
	private static final long serialVersionUID = 1L;

	
	public ConnectionError(String msg) {
		super(msg);
	}
	
	public ConnectionError(Exception ex) {
		super(ex);
	}
}

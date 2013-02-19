package org.lemsml.jlems.run;

public class ConnectionError extends Exception {
	private static final long serialVersionUID = 1L;

	
	public ConnectionError(String msg) {
		super(msg);
	}
	
	public ConnectionError(Exception ex) {
		super(ex);
	}
}

/**
 * 
 */
package org.lemsml.jlems.api;

import org.lemsml.jlems.core.sim.LEMSException;

/**
 * @author matteocantarelli
 *
 */
public class LEMSBuildException extends LEMSException
{
	private static final long serialVersionUID = 7270740174243762381L;
    
	public LEMSBuildException() {
		super();
	}
	public LEMSBuildException(Throwable arg0) {
		super(arg0);
	}
	public LEMSBuildException(String msg) {
		super(msg);
	}
	public LEMSBuildException(String msg, Throwable t) {
		super(msg, t);
	}
}

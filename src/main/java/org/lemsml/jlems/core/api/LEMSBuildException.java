/**
 * 
 */
package org.lemsml.jlems.core.api;

import org.lemsml.jlems.core.sim.LEMSException;

/**
 * @author matteocantarelli
 *
 */
public class LEMSBuildException extends LEMSException
{
    
	public LEMSBuildException(String msg) {
		super(msg);
	}
	public LEMSBuildException(String msg, Throwable t) {
		super(msg, t);
	}
}

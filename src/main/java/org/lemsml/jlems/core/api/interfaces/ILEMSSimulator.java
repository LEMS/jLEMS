/**
 * 
 */
package org.lemsml.jlems.core.api.interfaces;

import org.lemsml.jlems.core.api.LEMSExecutionException;

/**
 * @author matteocantarelli
 *
 */
public interface ILEMSSimulator
{

	public void advance(ILEMSStateInstance model, ILEMSRunConfiguration config ) throws LEMSExecutionException;
}

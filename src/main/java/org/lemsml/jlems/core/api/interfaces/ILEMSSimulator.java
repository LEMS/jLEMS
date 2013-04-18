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

	public void run(ILEMSRunConfiguration config, ILEMSStateInstance instance, ILEMSResultsContainer results) throws LEMSExecutionException;
	
}

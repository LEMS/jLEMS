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

	public void run(ILEMSResultsContainer results) throws LEMSExecutionException;
	
	public void advance(ILEMSResultsContainer results) throws LEMSExecutionException;
	
	public void initialize(ILEMSStateInstance instance,ILEMSRunConfiguration config) throws LEMSExecutionException;
}

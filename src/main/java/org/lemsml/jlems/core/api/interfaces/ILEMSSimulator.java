/**
 * 
 */
package org.lemsml.jlems.core.api.interfaces;

import org.lemsml.jlems.core.api.LEMSExecutionException;
import org.lemsml.jlems.core.api.Results;

/**
 * @author matteocantarelli
 *
 */
public interface ILEMSSimulator
{

	public void advance(ILEMSStateInstance model, Results results, ILEMSRunConfiguration config ) throws LEMSExecutionException;
}

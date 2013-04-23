/**
 * 
 */
package org.lemsml.jlems.core.api.interfaces;

import java.util.List;

import org.lemsml.jlems.core.api.AValue;

/**
 * @author matteocantarelli
 *
 */
public interface ILEMSResultsContainer
{

	public void addStateValue(IStateIdentifier state, AValue value);
	
	public List<AValue> getStateValues(IStateIdentifier state);
	
	public AValue getStateValue(IStateIdentifier state, int timeStep);


}

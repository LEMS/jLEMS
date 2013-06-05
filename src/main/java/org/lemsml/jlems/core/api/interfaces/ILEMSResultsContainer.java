/**
 * 
 */
package org.lemsml.jlems.core.api.interfaces;

import java.util.List;
import java.util.Map;

import org.lemsml.jlems.core.api.ALEMSValue;

/**
 * @author matteocantarelli
 *
 */
public interface ILEMSResultsContainer
{

	public void addStateValue(IStateIdentifier state, ALEMSValue value);
	
	public List<ALEMSValue> getStateValues(IStateIdentifier state);
	
	public ALEMSValue getStateValue(IStateIdentifier state, int timeStep);

	public Map<IStateIdentifier,List<ALEMSValue>> getStates();


}

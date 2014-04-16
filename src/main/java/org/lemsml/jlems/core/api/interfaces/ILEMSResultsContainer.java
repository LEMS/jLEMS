/**
 * 
 */
package org.lemsml.jlems.core.api.interfaces;

import java.util.Map;

import org.lemsml.jlems.core.api.ALEMSValue;
import org.lemsml.jlems.core.api.LEMSRecordedState;

/**
 * @author matteocantarelli
 *
 */
public interface ILEMSResultsContainer
{

	void addState(IStateIdentifier state, String dimension);
	
	public void addStateValue(IStateIdentifier state, ALEMSValue value);
	
	public LEMSRecordedState getState(IStateIdentifier state);
	
	public ALEMSValue getStateValue(IStateIdentifier state, int timeStep);

	public Map<IStateIdentifier,LEMSRecordedState> getStates();

	boolean hasState(IStateIdentifier state);

	


}

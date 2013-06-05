package org.lemsml.jlems.core.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lemsml.jlems.core.api.interfaces.ILEMSResultsContainer;
import org.lemsml.jlems.core.api.interfaces.IStateIdentifier;

/**
 * @author matteocantarelli
 *
 */
public class LEMSResultsContainer implements ILEMSResultsContainer
{

	private Map<IStateIdentifier, List<ALEMSValue>> _stateValues=new HashMap<IStateIdentifier, List<ALEMSValue>>();

	@Override
	public List<ALEMSValue> getStateValues(IStateIdentifier state)
	{
		return _stateValues.get(state);
	}

	@Override
	public ALEMSValue getStateValue(IStateIdentifier state, int timeStep)
	{
		return _stateValues.get(state).get(timeStep);
	}

	@Override
	public void addStateValue(IStateIdentifier state, ALEMSValue value)
	{
		if(!_stateValues.containsKey(state))
		{
			_stateValues.put(state, new ArrayList<ALEMSValue>());
		}
		_stateValues.get(state).add(value);
	}

	@Override
	public Map<IStateIdentifier, List<ALEMSValue>> getStates()
	{
		return _stateValues;
	}

	
	
}

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

	private Map<IStateIdentifier, List<AValue>> _stateValues=new HashMap<IStateIdentifier, List<AValue>>();

	@Override
	public List<AValue> getStateValues(IStateIdentifier state)
	{
		return _stateValues.get(state);
	}

	@Override
	public AValue getStateValue(IStateIdentifier state, int timeStep)
	{
		return _stateValues.get(state).get(timeStep);
	}

	@Override
	public void addStateValue(IStateIdentifier state, AValue value)
	{
		if(!_stateValues.containsKey(state))
		{
			_stateValues.put(state, new ArrayList<AValue>());
		}
		_stateValues.get(state).add(value);
				
		
	}

	
	
}

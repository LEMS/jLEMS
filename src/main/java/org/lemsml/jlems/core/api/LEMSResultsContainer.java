package org.lemsml.jlems.core.api;

import java.util.HashMap;
import java.util.Map;

import org.lemsml.jlems.core.api.interfaces.ILEMSResultsContainer;
import org.lemsml.jlems.core.api.interfaces.IStateIdentifier;

/**
 * @author matteocantarelli
 * 
 */
public class LEMSResultsContainer implements ILEMSResultsContainer
{

	private Map<IStateIdentifier, LEMSRecordedState> _recordedStates = new HashMap<IStateIdentifier, LEMSRecordedState>();

	@Override
	public LEMSRecordedState getState(IStateIdentifier state)
	{
		return _recordedStates.get(state);
	}

	@Override
	public ALEMSValue getStateValue(IStateIdentifier state, int timeStep)
	{
		return _recordedStates.get(state).getValue(timeStep);
	}

	@Override
	public void addState(IStateIdentifier state, String dimension)
	{
		_recordedStates.put(state, new LEMSRecordedState(state, dimension));
	}

	@Override
	public void addStateValue(IStateIdentifier state, ALEMSValue value)
	{
		_recordedStates.get(state).addValue(value);
	}

	@Override
	public Map<IStateIdentifier, LEMSRecordedState> getStates()
	{
		return _recordedStates;
	}

	@Override
	public boolean hasState(IStateIdentifier stateIdentifier)
	{
		return _recordedStates.containsKey(stateIdentifier);
	}

}

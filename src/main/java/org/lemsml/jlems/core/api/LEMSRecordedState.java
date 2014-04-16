/**
 * 
 */
package org.lemsml.jlems.core.api;

import java.util.ArrayList;
import java.util.List;

import org.lemsml.jlems.core.api.interfaces.IStateIdentifier;

/**
 * @author matteocantarelli
 *
 */
public class LEMSRecordedState
{

	List<ALEMSValue> _values=new ArrayList<ALEMSValue>();
	IStateIdentifier _stateIdentifier;
	String _dimension;
	
	/**
	 * @param _values
	 * @param _stateIdentifier
	 * @param _dimension
	 */
	public LEMSRecordedState(IStateIdentifier stateIdentifier, String dimension)
	{
		super();
		this._stateIdentifier = stateIdentifier;
		this._dimension = dimension;
	}
	
	/**
	 * @param value
	 */
	public void addValue(ALEMSValue value)
	{
		_values.add(value);
	}

	/**
	 * @param timeStep
	 * @return
	 */
	public ALEMSValue getValue(int timeStep)
	{
		return _values.get(timeStep);
	}

	/**
	 * @return
	 */
	public ALEMSValue getLastValue()
	{
		return _values.get(_values.size() - 1);
	}
	
	
}

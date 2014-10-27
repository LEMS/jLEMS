/**
 * 
 */
package org.lemsml.jlems.core.api;

import org.lemsml.jlems.core.api.interfaces.IStateIdentifier;
import org.lemsml.jlems.core.api.interfaces.IStateRecord;

/**
 * @author matteocantarelli
 *
 */
public class StateRecord implements IStateRecord
{

	private IStateIdentifier _state;
	private int _startTimeStep;
	private int _endTimeStep;
	
	
	public StateRecord(IStateIdentifier state, int startTimeStep, int endTimeStep)
	{
		super();
		this._state = state;
		this._startTimeStep = startTimeStep;
		this._endTimeStep = endTimeStep;
	}

	public StateRecord(IStateIdentifier state)
	{
		super();
		this._state = state;
		this._startTimeStep = 0;
		this._endTimeStep = -1;
	}

	@Override
	public String toString()
	{
		return "StateRecord ["+ _state + " startTimeStep=" + _startTimeStep + ", endTimeStep=" + _endTimeStep + "]";
	}

	public StateRecord(IStateIdentifier state, int startTimeStep)
	{
		super();
		this._state = state;
		this._startTimeStep = startTimeStep;
		this._endTimeStep = -1;
	}
	
	/* (non-Javadoc)
	 * @see org.lemsml.jlems.core.api.interfaces.IStateRecord#getStartRecordingTimeStep()
	 */
	@Override
	public int getStartRecordingTimeStep()
	{
		return _startTimeStep;
	}

	/* (non-Javadoc)
	 * @see org.lemsml.jlems.core.api.interfaces.IStateRecord#getEndRecordingTimeStep()
	 */
	@Override
	public int getEndRecordingTimeStep()
	{
		return _endTimeStep;
	}

	/* (non-Javadoc)
	 * @see org.lemsml.jlems.core.api.interfaces.IStateRecord#getState()
	 */
	@Override
	public IStateIdentifier getState()
	{
		return _state;
	}

	@Override
	public boolean record(int t)
	{
		if(t<_startTimeStep)
		{
			return false;
		}
		if(_endTimeStep!=-1 && t>_endTimeStep)
		{
			return false;
		}
		return true;
	}

}

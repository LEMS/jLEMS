/**
 * 
 */
package org.lemsml.jlems.core.api;

import java.util.ArrayList;
import java.util.Collection;

import org.lemsml.jlems.core.api.interfaces.ILEMSRunConfiguration;
import org.lemsml.jlems.core.api.interfaces.IStateRecord;

/**
 * @author matteocantarelli
 *
 */
public class LEMSRunConfiguration implements ILEMSRunConfiguration
{
	private double _timeStep;
	private double _runtime; 
	private Collection<IStateRecord> _statesToRecord;

	public LEMSRunConfiguration(double timeStep, double runtime)
	{
		super();
		this._timeStep = timeStep;
		this._runtime = runtime;
	}
	
	/* (non-Javadoc)
	 * @see org.lemsml.jlems.core.api.interfaces.ILEMSRunConfiguration#getTimestep()
	 */
	@Override
	public double getTimestep()
	{
		return _timeStep;
	}

	/* (non-Javadoc)
	 * @see org.lemsml.jlems.core.api.interfaces.ILEMSRunConfiguration#getRuntime()
	 */
	@Override
	public double getRuntime()
	{
		return _runtime;
	}

	/* (non-Javadoc)
	 * @see org.lemsml.jlems.core.api.interfaces.ILEMSRunConfiguration#addStateRecord(org.lemsml.jlems.core.api.interfaces.IStateRecord)
	 */
	@Override
	public void addStateRecord(IStateRecord stateRecord)
	{
		if(_statesToRecord==null)
		{
			_statesToRecord=new ArrayList<IStateRecord>();
		}
		_statesToRecord.add(stateRecord);
	}

	/* (non-Javadoc)
	 * @see org.lemsml.jlems.core.api.interfaces.ILEMSRunConfiguration#getRecordedStates()
	 */
	@Override
	public Collection<IStateRecord> getRecordedStates()
	{
		return _statesToRecord;
	}

}

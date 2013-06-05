/**
 * 
 */
package org.lemsml.jlems.core.api;

import java.util.Collection;

import org.lemsml.jlems.core.api.interfaces.ILEMSResultsContainer;
import org.lemsml.jlems.core.api.interfaces.ILEMSRunConfiguration;
import org.lemsml.jlems.core.api.interfaces.ILEMSSimulator;
import org.lemsml.jlems.core.api.interfaces.ILEMSStateInstance;
import org.lemsml.jlems.core.api.interfaces.IStateIdentifier;
import org.lemsml.jlems.core.api.interfaces.IStateRecord;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.EventManager;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.run.StateInstance;
import org.lemsml.jlems.core.run.StateWrapper;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.RunnableAccessor;

/**
 * @author matteocantarelli
 * 
 */
public class LEMSSimulator implements ILEMSSimulator
{

	private EventManager _eventManager;
	private StateInstance _rootState;
	private RunnableAccessor _runnableAccessor;
	private ILEMSRunConfiguration _config;
	private int _step=0;

	@Override
	public void run(ILEMSResultsContainer results) throws LEMSExecutionException
	{
		E.info("Simulation start");
		for(double t = 0; t < _config.getRuntime(); t += _config.getTimestep())
		{
			if(t > 0)
			{
				advance(results);
			}
		}
		E.info("Simulation end, " + _step + " steps simulated!");
	}

	@Override
	public void advance(ILEMSResultsContainer results) throws LEMSExecutionException
	{
		try
		{
			_eventManager.advance(_step);
			
			_rootState.advance(null, _step*_config.getTimestep(), _config.getTimestep());

			if(_config.getRecordedStates() != null)
			{
				for(IStateRecord stateToRecord : _config.getRecordedStates())
				{
					if(stateToRecord.record(_step))
					{
						StateWrapper sw = _runnableAccessor.getStateWrapper(stateToRecord.getState().getStatePath());
						double value = sw.getValue();
						results.addStateValue(stateToRecord.getState(), new LEMSDoubleValue(value));
					}
				}
			}
			_step++;
		}
		catch(RuntimeError e)
		{
			throw new LEMSExecutionException(e);
		}
		catch(ContentError e)
		{
			throw new LEMSExecutionException(e);
		}
		catch(ConnectionError e)
		{
			throw new LEMSExecutionException(e);
		}

	}


	@Override
	public void initialize(ILEMSStateInstance instance, ILEMSRunConfiguration config) throws LEMSExecutionException
	{
		_eventManager = EventManager.getInstance();
		_rootState = (StateInstance) instance;
		_runnableAccessor = new RunnableAccessor(_rootState);
		_config = config;
		try
		{
			_rootState.initialize(null);
		}
		catch(RuntimeError e)
		{
			throw new LEMSExecutionException(e);
		}
		catch(ContentError e)
		{
			throw new LEMSExecutionException(e);
		}

	}

	@Override
	public Collection<IStateIdentifier> getAvailableStates()
	{
//		_runnableAccessor.getStateWrapper(path)
		return null;
	}
}

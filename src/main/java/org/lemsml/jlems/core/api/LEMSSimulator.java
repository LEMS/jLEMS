/**
 * 
 */
package org.lemsml.jlems.core.api;

import org.lemsml.jlems.core.api.interfaces.ILEMSResultsContainer;
import org.lemsml.jlems.core.api.interfaces.ILEMSRunConfiguration;
import org.lemsml.jlems.core.api.interfaces.ILEMSSimulator;
import org.lemsml.jlems.core.api.interfaces.ILEMSStateInstance;
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

	@Override
	public void run(ILEMSRunConfiguration config, ILEMSStateInstance instance, ILEMSResultsContainer results) throws LEMSExecutionException
	{
		EventManager eventManager = EventManager.getInstance();
		StateInstance rootState = (StateInstance) instance;
		RunnableAccessor runnableAccessor = new RunnableAccessor(rootState);

		try
		{
			rootState.initialize(null);

			E.info("Simulation start");

			int steps = 0;
			for(double t = 0; t < config.getRuntime(); t += config.getTimestep(), steps++)
			{
				if(t > 0)
				{
					eventManager.advance(t);
					rootState.advance(null, t, config.getTimestep());

					if(config.getRecordedStates() != null)
					{
						for(IStateRecord stateToRecord : config.getRecordedStates())
						{
							if(stateToRecord.record(steps))
							{
								StateWrapper sw = runnableAccessor.getStateWrapper(stateToRecord.getState().getStatePath());
								double value = sw.getValue();
								results.addStateValue(stateToRecord.getState(), new DoubleValue(value));
							}
						}
					}
				}
			}
			
			E.info("Simulation end, " + steps + " steps simulated!");

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

}

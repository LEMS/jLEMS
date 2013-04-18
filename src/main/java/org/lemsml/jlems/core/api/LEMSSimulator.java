/**
 * 
 */
package org.lemsml.jlems.core.api;

import java.util.ArrayList;

import org.lemsml.jlems.core.api.interfaces.ILEMSResultsContainer;
import org.lemsml.jlems.core.api.interfaces.ILEMSRunConfiguration;
import org.lemsml.jlems.core.api.interfaces.ILEMSSimulator;
import org.lemsml.jlems.core.api.interfaces.ILEMSStateInstance;
import org.lemsml.jlems.core.display.DataViewer;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.out.ResultWriter;
import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.EventManager;
import org.lemsml.jlems.core.run.RunConfig;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.run.RuntimeRecorder;
import org.lemsml.jlems.core.run.StateInstance;
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
		RunConfig runConfig = (RunConfig) config; // FIXME: runconfig should not contain runtime recorders
		RunnableAccessor runnableAccessor = new RunnableAccessor(rootState);

		ArrayList<RuntimeRecorder> recorders = runConfig.getRecorders();

		for (RuntimeRecorder runtimeRecorder : recorders)
		{
			String disp = runtimeRecorder.getDisplay();

			DataViewer dataViewer = results.getDataViewer(disp);
			if (dataViewer != null)
			{
				try
				{
					runtimeRecorder.connectRunnable(runnableAccessor, dataViewer);
				}
				catch (ConnectionError e)
				{
					throw new LEMSExecutionException(e);
				}

			}

			ResultWriter resultWriter = results.getResultWriter(disp);
			if (resultWriter != null)
			{
				resultWriter.addedRecorder();
				try
				{
					runtimeRecorder.connectRunnable(runnableAccessor, resultWriter);
				}
				catch (ConnectionError e)
				{
					throw new LEMSExecutionException(e);
				}

				E.info("Connected runnable to " + disp + " " + resultWriter);
			}

			if (dataViewer == null && resultWriter == null)
			{
				throw new LEMSExecutionException(new ConnectionError("No data viewer or result writer " + disp + " for " + runtimeRecorder + " was found"));
			}
		}

		try
		{
			rootState.initialize(null);

			E.info("Simulation start");
			
			int steps=0;
			for (double t = 0; t < config.getRuntime(); t += config.getTimestep(), steps++)
			{
				if (t > 0)
				{
					eventManager.advance(t);
					rootState.advance(null, t, config.getTimestep());
				}
				results.advanceResultWriters(t);

				for (RuntimeRecorder rr : recorders)
				{
					rr.appendState(t);
				}

			}
			E.info("Simulation end, "+steps+" steps simulated!");
			
			results.closeResultWriters();
		}
		catch (RuntimeError e)
		{
			throw new LEMSExecutionException(e);
		}
		catch (ContentError e)
		{
			throw new LEMSExecutionException(e);
		}
	}

}

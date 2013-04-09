/**
 * 
 */
package org.lemsml.jlems.core.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lemsml.jlems.core.api.interfaces.ILEMSRunConfiguration;
import org.lemsml.jlems.core.api.interfaces.ILEMSSimulator;
import org.lemsml.jlems.core.api.interfaces.ILEMSStateInstance;
import org.lemsml.jlems.core.display.DataViewPort;
import org.lemsml.jlems.core.display.DataViewer;
import org.lemsml.jlems.core.display.DataViewerFactory;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.out.ResultWriter;
import org.lemsml.jlems.core.out.ResultWriterFactory;
import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.EventManager;
import org.lemsml.jlems.core.run.RunConfig;
import org.lemsml.jlems.core.run.RuntimeDisplay;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.run.RuntimeOutput;
import org.lemsml.jlems.core.run.RuntimeRecorder;
import org.lemsml.jlems.core.run.StateInstance;
import org.lemsml.jlems.core.run.StateType;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.DisplayCollector;
import org.lemsml.jlems.core.sim.OutputCollector;
import org.lemsml.jlems.core.sim.RunConfigCollector;
import org.lemsml.jlems.core.sim.RunnableAccessor;

/**
 * @author matteocantarelli
 * 
 */
public class LEMSSimulator implements ILEMSSimulator
{


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lemsml.jlems.core.api.interfaces.ILEMSSimulator#advance(org.lemsml.jlems.core.api.interfaces.ILEMSStateInstance, org.lemsml.jlems.core.api.interfaces.ILEMSRunConfiguration)
	 */
	@Override
	public void advance(ILEMSStateInstance instance, Results results, ILEMSRunConfiguration config) throws LEMSExecutionException
	{
		EventManager eventManager = EventManager.getInstance();

		StateInstance rootState = (StateInstance) instance;
		
		
		RunConfig rc = (RunConfig) config;

		RunnableAccessor ra = new RunnableAccessor(rootState);
		ArrayList<RuntimeRecorder> recorders = rc.getRecorders();

		for (RuntimeRecorder rr : recorders)
		{
			String disp = rr.getDisplay();
			if (results.getDvHM().containsKey(disp))
			{
				try
				{
					rr.connectRunnable(ra, results.getDvHM().get(disp));
				}
				catch (ConnectionError e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			else if (results.getRwHM().containsKey(disp))
			{
				ResultWriter rw = results.getRwHM().get(disp);
				rw.addedRecorder();
				try
				{
					rr.connectRunnable(ra, rw);
				}
				catch (ConnectionError e)
				{
					throw new LEMSExecutionException(e);
				}

				E.info("Connected runnable to " + disp + " " + results.getRwHM().get(disp));

			}
			else
			{
				throw new LEMSExecutionException(new ConnectionError("No such data viewer " + disp + " needed for " + rr));
			}
		}

		double dt = rc.getTimestep();
		double t = 0;

		try
		{
			rootState.initialize(null);
			eventManager.advance(t);
			rootState.advance(null, t, dt);
			for (ResultWriter rw : results.getResultWriters())
			{
				rw.advance(t);
			}

			for (RuntimeRecorder rr : recorders)
			{
				rr.appendState(t);
			}

			for (ResultWriter rw : results.getResultWriters())
			{
				rw.close();
			}
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

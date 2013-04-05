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
	public void advance(ILEMSStateInstance instance, ILEMSRunConfiguration config) throws LEMSExecutionException
	{

		Map<String, DataViewer> dvHM=new HashMap<String, DataViewer>();
		Map<String, ResultWriter> rwHM=new HashMap<String, ResultWriter>();
		EventManager eventManager = EventManager.getInstance();

		List<ResultWriter> resultWriters = new ArrayList<ResultWriter>();

		StateInstance rootState = (StateInstance) instance;
		
		setup(dvHM, rwHM, resultWriters, rootState.getStateType());
		RunConfig rc = (RunConfig) config;

		RunnableAccessor ra = new RunnableAccessor(rootState);
		ArrayList<RuntimeRecorder> recorders = rc.getRecorders();

		for (RuntimeRecorder rr : recorders)
		{
			String disp = rr.getDisplay();
			if (dvHM.containsKey(disp))
			{
				try
				{
					rr.connectRunnable(ra, dvHM.get(disp));
				}
				catch (ConnectionError e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			else if (rwHM.containsKey(disp))
			{
				ResultWriter rw = rwHM.get(disp);
				rw.addedRecorder();
				try
				{
					rr.connectRunnable(ra, rw);
				}
				catch (ConnectionError e)
				{
					throw new LEMSExecutionException(e);
				}

				E.info("Connected runnable to " + disp + " " + rwHM.get(disp));

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
			for (ResultWriter rw : resultWriters)
			{
				rw.advance(t);
			}

			for (RuntimeRecorder rr : recorders)
			{
				rr.appendState(t);
			}

			for (ResultWriter rw : resultWriters)
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

	private void setup(Map<String, DataViewer> dvHM, Map<String, ResultWriter> rwHM, List<ResultWriter> resultWriters, StateType rootState)
	{
		// collect everything in the StateType tree that makes a display
		ArrayList<RuntimeDisplay> runtimeDisplays = new ArrayList<RuntimeDisplay>();
		DisplayCollector oc = new DisplayCollector(runtimeDisplays);
		rootState.visitAll(oc);

		// build the displays and keep them in dvHM
		for (RuntimeDisplay ro : runtimeDisplays)
		{
			DataViewer dv = DataViewerFactory.getFactory().newDataViewer(ro.getTitle());
			dvHM.put(ro.getID(), dv);
			if (dv instanceof DataViewPort)
			{
				((DataViewPort) dv).setRegion(ro.getBox());
			}
		}

		// collect everything in the StateType tree that records something
		ArrayList<RuntimeOutput> runtimeOutputs = new ArrayList<RuntimeOutput>();
		OutputCollector oco = new OutputCollector(runtimeOutputs);
		rootState.visitAll(oco);

		// build the displays and keep them in dvHM
		for (RuntimeOutput ro : runtimeOutputs)
		{
			ResultWriter rw = ResultWriterFactory.getFactory().newResultWriter(ro);
			rwHM.put(ro.getID(), rw);
			resultWriters.add(rw);
		}
	}

}

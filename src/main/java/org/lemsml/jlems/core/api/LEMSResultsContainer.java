package org.lemsml.jlems.core.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.lemsml.jlems.core.api.interfaces.ILEMSResultsContainer;
import org.lemsml.jlems.core.display.DataViewPort;
import org.lemsml.jlems.core.display.DataViewer;
import org.lemsml.jlems.core.display.DataViewerFactory;
import org.lemsml.jlems.core.out.ResultWriter;
import org.lemsml.jlems.core.out.ResultWriterFactory;
import org.lemsml.jlems.core.run.RuntimeDisplay;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.run.RuntimeOutput;
import org.lemsml.jlems.core.run.StateType;
import org.lemsml.jlems.core.sim.DisplayCollector;
import org.lemsml.jlems.core.sim.OutputCollector;

/**
 * @author matteocantarelli
 *
 */
public class LEMSResultsContainer implements ILEMSResultsContainer
{

	private Map<String, DataViewer> _dvHM=new HashMap<String, DataViewer>();
	private Map<String, ResultWriter> _rwHM=new HashMap<String, ResultWriter>();
	
	public LEMSResultsContainer(StateType rootStateType)
	{
		super();
		createOutputConfig(rootStateType);
	}
	
	private void createOutputConfig(StateType rootState)
	{
		// collect everything in the StateType tree that makes a display
		ArrayList<RuntimeDisplay> runtimeDisplays = new ArrayList<RuntimeDisplay>();
		DisplayCollector oc = new DisplayCollector(runtimeDisplays);
		rootState.visitAll(oc);

		// build the displays and keep them in dvHM
		for (RuntimeDisplay ro : runtimeDisplays)
		{
			DataViewer dv = DataViewerFactory.getFactory().newDataViewer(ro.getTitle());
			_dvHM.put(ro.getID(), dv);
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
			_rwHM.put(ro.getID(), rw);
		}
	}

	@Override
	public void closeResultWriters() throws LEMSExecutionException
	{

		for (ResultWriter rw : _rwHM.values())
		{
			try
			{
				rw.close();
			}
			catch (RuntimeError e)
			{
				throw new LEMSExecutionException(e);
			}
		}
		
	}

	@Override
	public DataViewer getDataViewer(String dataViewer)
	{
		return _dvHM.get(dataViewer);
	}

	@Override
	public ResultWriter getResultWriter(String resultWriter)
	{
		return _rwHM.get(resultWriter);
	}

	@Override
	public void advanceResultWriters(double timeStep) throws LEMSExecutionException
	{
		for (ResultWriter rw : _rwHM.values())
		{
			try
			{
				rw.advance(timeStep);
			}
			catch (RuntimeError e)
			{
				throw new LEMSExecutionException(e);
			}
		}
	}
	
	
}

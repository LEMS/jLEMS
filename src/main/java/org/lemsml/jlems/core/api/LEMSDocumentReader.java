package org.lemsml.jlems.core.api;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.lemsml.jlems.core.api.interfaces.ILEMSDocument;
import org.lemsml.jlems.core.api.interfaces.ILEMSDocumentReader;
import org.lemsml.jlems.core.api.interfaces.ILEMSRunConfiguration;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.reader.LemsFactory;
import org.lemsml.jlems.core.run.RunConfig;
import org.lemsml.jlems.core.run.RuntimeDisplay;
import org.lemsml.jlems.core.run.RuntimeRecorder;
import org.lemsml.jlems.core.run.StateType;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.DisplayCollector;
import org.lemsml.jlems.core.sim.RunConfigCollector;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.Target;
import org.lemsml.jlems.core.xml.XMLElement;
import org.lemsml.jlems.core.xml.XMLElementReader;
import org.lemsml.jlems.io.reader.FileInclusionReader;
import org.lemsml.jlems.io.reader.URLInclusionReader;

public class LEMSDocumentReader implements ILEMSDocumentReader
{

	@Override
	public ILEMSDocument readModel(URL modelURL) throws IOException, ContentError
	{
		if(modelURL.getProtocol().equals("file"))
		{
			return readModel(new File(modelURL.getPath()));
		}
		URLInclusionReader uir = new URLInclusionReader(modelURL);
		return readModel(uir.read());
	}

	@Override
	public ILEMSDocument readModel(String modelContent) throws IOException, ContentError
	{
		// TODO tmp - make reader cope without extra spaces
		XMLElementReader exmlr = new XMLElementReader(modelContent + "    ");
		XMLElement xel = exmlr.getRootElement();

		LemsFactory lf = new LemsFactory();
		Lems lems = lf.buildLemsFromXMLElement(xel);
		return lems;
	}

	public static ILEMSRunConfiguration getLEMSRunConfiguration(ILEMSDocument lemsDocument) throws ContentError, ParseError
	{
		Lems lems = (Lems) lemsDocument;

		Target dr = lems.getTarget();
		Component simCpt = dr.getComponent();

		if(simCpt == null)
		{
			E.error("No such component: " + dr.component + " as referred to by default simulation.");
			E.error(lems.textSummary());
			throw new ContentError("No such component " + dr.component);
		}
		StateType rootBehavior = simCpt.getStateType();

		// collect everything in the StateType tree that makes a display
		ArrayList<RuntimeDisplay> runtimeDisplays = new ArrayList<RuntimeDisplay>();
		DisplayCollector oc = new DisplayCollector(runtimeDisplays);
		rootBehavior.visitAll(oc);

		ArrayList<RunConfig> runConfigs = new ArrayList<RunConfig>();
		RunConfigCollector rcc = new RunConfigCollector(runConfigs);
		rootBehavior.visitAll(rcc);

		ILEMSRunConfiguration runConfig = new LEMSRunConfiguration(runConfigs.get(0).getTimestep(), runConfigs.get(0).getRuntime());
		for(RuntimeRecorder rtr : runConfigs.get(0).getRecorders())
		{
			runConfig.addStateRecord(new StateRecord(new StateIdentifier(rtr.getQuantity())));
		}
		return runConfig;
	}

	public static String getTarget(ILEMSDocument lemsDocument) throws ContentError, ParseError
	{
		Lems lems = (Lems) lemsDocument;

		Target dr = lems.getTarget();
		Component simCpt = dr.getComponent();

		if(simCpt == null)
		{
			E.error("No such component: " + dr.component + " as referred to by default simulation.");
			E.error(lems.textSummary());
			throw new ContentError("No such component " + dr.component);
		}
		StateType rootBehavior = simCpt.getStateType();

		// collect everything in the StateType tree that makes a display
		ArrayList<RuntimeDisplay> runtimeDisplays = new ArrayList<RuntimeDisplay>();
		DisplayCollector oc = new DisplayCollector(runtimeDisplays);
		rootBehavior.visitAll(oc);

		ArrayList<RunConfig> runConfigs = new ArrayList<RunConfig>();
		RunConfigCollector rcc = new RunConfigCollector(runConfigs);
		rootBehavior.visitAll(rcc);
		
		return runConfigs.get(0).getTarget().getComponentID();
	}

	@Override
	public ILEMSDocument readModel(File modelFile) throws IOException, ContentError
	{
		FileInclusionReader fir = new FileInclusionReader(modelFile);
		return readModel(fir.read());
	}

}

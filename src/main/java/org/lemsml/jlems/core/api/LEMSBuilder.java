package org.lemsml.jlems.core.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lemsml.jlems.core.api.interfaces.ILEMSBuildConfiguration;
import org.lemsml.jlems.core.api.interfaces.ILEMSBuildOptions;
import org.lemsml.jlems.core.api.interfaces.ILEMSBuilder;
import org.lemsml.jlems.core.api.interfaces.ILEMSDocument;
import org.lemsml.jlems.core.api.interfaces.ILEMSResultsContainer;
import org.lemsml.jlems.core.api.interfaces.ILEMSRunConfiguration;
import org.lemsml.jlems.core.api.interfaces.ILEMSStateInstance;
import org.lemsml.jlems.core.api.interfaces.ILEMSStateType;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.Constants;
import org.lemsml.jlems.core.run.RunConfig;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.run.StateInstance;
import org.lemsml.jlems.core.run.StateType;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.RunConfigCollector;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.Meta;
import org.lemsml.jlems.core.type.Target;

/**
 * This class implements the API interface to build a LEMS document. At the moment we don't have separate document classes which just contain the data that can be simulated but this class and the API
 * in general aims at hiding this. Subsequent refactor will have to split the model from the logic!
 * 
 * @author matteocantarelli
 * 
 */
public class LEMSBuilder implements ILEMSBuilder
{

	private List<ILEMSDocument> _documents = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lemsml.jlems.core.api.interfaces.ILEMSBuilder#addDocument(org.lemsml.jlems.core.api.interfaces.ILEMSDocument)
	 */
	@Override
	public void addDocument(ILEMSDocument lemsDocument)
	{
		if (_documents == null)
		{
			_documents = new ArrayList<ILEMSDocument>();
		}
		_documents.add(lemsDocument);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lemsml.jlems.core.api.interfaces.ILEMSBuilder#build(org.lemsml.jlems.core.api.interfaces.ILEMSBuildOptions)
	 */
	@Override
	public Collection<ILEMSStateInstance> build(ILEMSBuildConfiguration config, ILEMSBuildOptions options) throws LEMSBuildException
	{
		// FIXME ILEMSBuildOptions should be used to control the flow in case we decide to flatten or not for instance
		return createExecutableInstance(createLEMSStates(config,options), options);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lemsml.jlems.core.api.interfaces.ILEMSBuilder#createLEMSStates(org.lemsml.jlems.core.api.interfaces.ILEMSBuildOptions)
	 */
	@Override
	public Map<ILEMSStateType, ILEMSDocument> createLEMSStates(ILEMSBuildConfiguration config, ILEMSBuildOptions options) throws LEMSBuildException
	{
		Map<ILEMSStateType, ILEMSDocument> stateMap = new HashMap<ILEMSStateType, ILEMSDocument>();
		for (ILEMSDocument lemsDoc : _documents)
		{
			Lems lems = (Lems) lemsDoc;
			lems.setResolveModeLoose();
			try
			{
				lems.deduplicate();
				lems.resolve();
				lems.evaluateStatic();
			}
			catch (ContentError e)
			{
				throw new LEMSBuildException(e);
			}
			catch (ParseError e)
			{
				throw new LEMSBuildException(e);
			}

			for (Component cpt : lems.getComponents())
			{

				if (cpt.getID().equals(config.getSpecifiedTarget()))
				{
					ComponentType ct = cpt.getComponentType();
					try
					{
						// all the knowledge of how to build a state type should be outside the document
						StateType stateType = ct.makeStateType(cpt);

						boolean mflat = options.isOn(LEMSBuildOptionsEnum.FLATTEN);

						if (cpt != null)
						{
							E.info("checking metas " + cpt.getID() + " " + cpt.metas.size());

							for (Meta m : cpt.metas.getContents())
							{
								// FIXME this kind of parsing once that the model is built is suspicious
								HashMap<String, String> hm = m.getAttributes();
								if (hm.containsKey("method")) // FIXME Strings are EVIL
								{
									String val = hm.get("method").toLowerCase(); // FIXME Strings are EVIL
									if (val.equals("rk4")) // FIXME Strings are EVIL
									{
										mflat = mflat && true; // the result of attempt a flattening depends on whether it is required to begin with
										E.info("Got meta for jlems: " + val);

									}
									else if (val.equals("eulertree")) // FIXME Strings are EVIL
									{
										mflat = mflat && false; // the result of attempt a flattening depends on whether it is required to begin with
										E.info("Got meta for jlems: " + val);

									}
									else
									{
										E.warning("unrecognized method " + val);
									}
								}

							}
						}

						stateMap.put(stateType, lems);

					}
					catch (ContentError e)
					{
						throw new LEMSBuildException(e);
					}
					catch (ParseError e)
					{
						throw new LEMSBuildException(e);
					}
				}
			}

		}
		return stateMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lemsml.jlems.core.api.interfaces.ILEMSBuilder#createExecutableInstance()
	 */
	@Override
	public Collection<ILEMSStateInstance> createExecutableInstance(Map<ILEMSStateType, ILEMSDocument> stateMap, ILEMSBuildOptions options) throws LEMSBuildException
	{
		Collection<ILEMSStateInstance> stateInstances = new ArrayList<ILEMSStateInstance>();
		for (ILEMSStateType stateTypeDoc : stateMap.keySet())
		{
			// FIXME Static is EVIL, refactor constants so that they can be part of a normal data flow

			// FIXME I am using a map to link statetype with lems since from the old code it looks like some information in lems not
			// contained in state type (the constants) is required in order to create the StateInstance. This is creating problems
			// since forcing us to have a state in the class which would not be needed if StateType would have all the information required
			// If it's not possible to have this process stateless (why?) then the state should be stored in the builder class and not in Lems objects.
			// All of this will have to change when we split model and logic
			boolean mflat = options.isOn(LEMSBuildOptionsEnum.FLATTEN);
			StateType targetBehavior = ((StateType) stateTypeDoc);
			if (mflat)
			{
				targetBehavior = ((StateType) stateTypeDoc).getConsolidatedStateType("root");
			}

			Lems lems = (Lems) stateMap.get(stateTypeDoc); // FIXME Castings like this show there is something wrong...
			Constants.setConstantsHM(lems.getConstantsValueHM());
			try
			{
				StateInstance stateInstance = targetBehavior.newInstance(); // again this should all be documents without logic
				stateInstance.checkBuilt(); // this checks that all the other components that are needed have also been built
				stateInstances.add(stateInstance);
			}
			catch (ContentError e)
			{
				throw new LEMSBuildException(e);
			}
			catch (ConnectionError e)
			{
				throw new LEMSBuildException(e);
			}
			catch (RuntimeError e)
			{
				throw new LEMSBuildException(e);
			}
		}
		return stateInstances;
	}

	@Override
	public ILEMSRunConfiguration getRunConfiguration(ILEMSDocument lemsDocument) throws LEMSBuildException
	{
		Lems lems = (Lems) lemsDocument;
		lems.setResolveModeLoose();
		
		Target target;
		try
		{
			target = lems.getTarget();
			Component cpt=target.getComponent();
			ComponentType ct=cpt.getComponentType();
			StateType stateType = ct.makeStateType(cpt);
			List<RunConfig> runConfigs = new ArrayList<RunConfig>();
			RunConfigCollector rcc = new RunConfigCollector(runConfigs);
			stateType.visitAll(rcc);
			return runConfigs.get(0);
		}
		catch (ContentError e)
		{
			throw new LEMSBuildException(e);
		}
		catch (ParseError e)
		{
			throw new LEMSBuildException(e);
		}
	}
	

	@Override
	public ILEMSResultsContainer getResultsContainer(ILEMSDocument lemsDocument) throws LEMSBuildException
	{
		Lems lems = (Lems) lemsDocument;
		lems.setResolveModeLoose();
		
		Target target;
		try
		{
			target = lems.getTarget();
			Component cpt=target.getComponent();
			ComponentType ct=cpt.getComponentType();
			StateType stateType = ct.makeStateType(cpt);
			return new LEMSResultsContainer(stateType);
		}
		catch (ContentError e)
		{
			throw new LEMSBuildException(e);
		}
		catch (ParseError e)
		{
			throw new LEMSBuildException(e);
		}
	}

}

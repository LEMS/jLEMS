/**
 * 
 */
package org.lemsml.jlems.test.core.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.lemsml.jlems.core.api.LEMSBuildException;
import org.lemsml.jlems.core.api.LEMSBuildOptions;
import org.lemsml.jlems.core.api.LEMSBuildOptionsEnum;
import org.lemsml.jlems.core.api.LEMSBuilder;
import org.lemsml.jlems.core.api.LEMSDocumentReader;
import org.lemsml.jlems.core.api.LEMSExecutionException;
import org.lemsml.jlems.core.api.LEMSSimulator;
import org.lemsml.jlems.core.api.Results;
import org.lemsml.jlems.core.api.interfaces.ILEMSBuildOptions;
import org.lemsml.jlems.core.api.interfaces.ILEMSDocument;
import org.lemsml.jlems.core.api.interfaces.ILEMSRunConfiguration;
import org.lemsml.jlems.core.api.interfaces.ILEMSSimulator;
import org.lemsml.jlems.core.api.interfaces.ILEMSStateInstance;
import org.lemsml.jlems.core.api.interfaces.ILEMSStateType;
import org.lemsml.jlems.core.run.RunConfig;
import org.lemsml.jlems.core.run.StateType;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.RunConfigCollector;

/**
 * @author matteocantarelli
 * 
 */
public class LEMSSimulatorTest
{

	/**
	 * Test method for {@link org.lemsml.jlems.core.api.LEMSSimulator#advance(org.lemsml.jlems.core.api.interfaces.ILEMSStateInstance, org.lemsml.jlems.core.api.interfaces.ILEMSRunConfiguration)}.
	 */
	@Test
	public void testAdvance()
	{
		LEMSDocumentReader reader = new LEMSDocumentReader();
		LEMSBuilder builder = new LEMSBuilder();
		try
		{
			ILEMSDocument model = reader.readModel(this.getClass().getResource("/example1.xml"));
			assertNotNull(model);
			
			builder.addDocument(model);
			ILEMSBuildOptions options = new LEMSBuildOptions();
			options.addBuildOption(LEMSBuildOptionsEnum.FLATTEN);
			
			Map<ILEMSStateType, ILEMSDocument> stateMap = builder.createLEMSStates(options);
			Collection<ILEMSStateInstance> stateInstances = builder.createExecutableInstance(stateMap, options);
			
			assertNotNull(stateInstances);
			assertFalse(stateInstances.isEmpty());
			
			List<RunConfig> runConfigs = new ArrayList<RunConfig>();
			Results results=null;
			for(ILEMSStateType stateType:stateMap.keySet())
			{
				RunConfigCollector rcc = new RunConfigCollector(runConfigs);
				((StateType)stateType).visitAll(rcc);
				results=new Results((StateType)stateType);	
			}

			ILEMSSimulator simulator = new LEMSSimulator();
			for (ILEMSStateInstance instance : stateInstances)
			{
				for (ILEMSRunConfiguration rc : runConfigs)
				{
					try
					{
						for (int i = 0; i < 100; i++)
						{
							simulator.advance(instance, results, rc);
						}
					}
					catch (LEMSExecutionException e)
					{
						fail(e.getMessage());
					}
				}
			}

		}
		catch (IOException e)
		{
			fail(e.getMessage());
		}
		catch (ContentError e)
		{
			fail(e.getMessage());
		}
		catch (LEMSBuildException e)
		{
			fail(e.getMessage());
		}
	}

}

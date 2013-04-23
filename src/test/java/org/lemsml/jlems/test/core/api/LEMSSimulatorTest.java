/**
 * 
 */
package org.lemsml.jlems.test.core.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;
import org.lemsml.jlems.core.api.LEMSBuildConfiguration;
import org.lemsml.jlems.core.api.LEMSBuildException;
import org.lemsml.jlems.core.api.LEMSBuildOptions;
import org.lemsml.jlems.core.api.LEMSBuildOptionsEnum;
import org.lemsml.jlems.core.api.LEMSBuilder;
import org.lemsml.jlems.core.api.LEMSDocumentReader;
import org.lemsml.jlems.core.api.LEMSExecutionException;
import org.lemsml.jlems.core.api.LEMSResultsContainer;
import org.lemsml.jlems.core.api.LEMSRunConfiguration;
import org.lemsml.jlems.core.api.LEMSSimulator;
import org.lemsml.jlems.core.api.StateIdentifier;
import org.lemsml.jlems.core.api.StateRecord;
import org.lemsml.jlems.core.api.interfaces.ILEMSBuildConfiguration;
import org.lemsml.jlems.core.api.interfaces.ILEMSBuildOptions;
import org.lemsml.jlems.core.api.interfaces.ILEMSBuilder;
import org.lemsml.jlems.core.api.interfaces.ILEMSDocument;
import org.lemsml.jlems.core.api.interfaces.ILEMSDocumentReader;
import org.lemsml.jlems.core.api.interfaces.ILEMSResultsContainer;
import org.lemsml.jlems.core.api.interfaces.ILEMSRunConfiguration;
import org.lemsml.jlems.core.api.interfaces.ILEMSSimulator;
import org.lemsml.jlems.core.api.interfaces.ILEMSStateInstance;
import org.lemsml.jlems.core.api.interfaces.IStateIdentifier;
import org.lemsml.jlems.core.sim.ContentError;

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
	public void testRun()
	{	
		//SwingDataViewerFactory.initialize();
		ILEMSDocumentReader reader = new LEMSDocumentReader();
		ILEMSBuilder builder = new LEMSBuilder();
		try
		{
			
			ILEMSDocument model = reader.readModel(this.getClass().getResource("/example1.xml"));
			assertNotNull(model);
			
			builder.addDocument(model);
			ILEMSBuildOptions options = new LEMSBuildOptions();
			options.addBuildOption(LEMSBuildOptionsEnum.FLATTEN);
			
			ILEMSBuildConfiguration config = new LEMSBuildConfiguration("net1");
			
			Collection<ILEMSStateInstance> stateInstances = builder.build(config, options);

			assertNotNull(stateInstances);
			assertFalse(stateInstances.isEmpty());
			
			ILEMSSimulator simulator = new LEMSSimulator();
			for (ILEMSStateInstance instance : stateInstances)
			{			
				ILEMSRunConfiguration runConfig=new LEMSRunConfiguration(0.00005d, 0.08d);
				
				IStateIdentifier tsince=new StateIdentifier("p1[0]/tsince");
				IStateIdentifier p3v=new StateIdentifier("p3[0]/v");
				IStateIdentifier hhpopv=new StateIdentifier("hhpop[0]/v");
				
				runConfig.addStateRecord(new StateRecord(tsince));
				runConfig.addStateRecord(new StateRecord(p3v));
				runConfig.addStateRecord(new StateRecord(hhpopv));
				
				ILEMSResultsContainer results=new LEMSResultsContainer();
				
				simulator.run(runConfig, instance, results);
				
				assertNotNull(results.getStateValues(tsince));
				assertNotNull(results.getStateValues(p3v));
				assertNotNull(results.getStateValues(hhpopv));
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
		catch (LEMSExecutionException e)
		{
			fail(e.getMessage());
		}
	}

}

/**
 * 
 */
package org.lemsml.jlems.test.core.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.lemsml.jlems.core.api.LEMSBuildConfiguration;
import org.lemsml.jlems.core.api.LEMSBuildException;
import org.lemsml.jlems.core.api.LEMSBuildOptions;
import org.lemsml.jlems.core.api.LEMSBuildOptionsEnum;
import org.lemsml.jlems.core.api.LEMSBuilder;
import org.lemsml.jlems.core.api.LEMSDocumentReader;
import org.lemsml.jlems.core.api.LEMSExecutionException;
import org.lemsml.jlems.core.api.LEMSResultsContainer;
import org.lemsml.jlems.core.api.LEMSSimulator;
import org.lemsml.jlems.core.api.StateIdentifier;
import org.lemsml.jlems.core.api.interfaces.ILEMSBuildConfiguration;
import org.lemsml.jlems.core.api.interfaces.ILEMSBuildOptions;
import org.lemsml.jlems.core.api.interfaces.ILEMSDocument;
import org.lemsml.jlems.core.api.interfaces.ILEMSResultsContainer;
import org.lemsml.jlems.core.api.interfaces.ILEMSRunConfiguration;
import org.lemsml.jlems.core.api.interfaces.ILEMSSimulator;
import org.lemsml.jlems.core.api.interfaces.ILEMSStateInstance;
import org.lemsml.jlems.core.api.interfaces.IStateIdentifier;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.sim.ContentError;

/**
 * @author matteocantarelli
 * 
 */
public class LEMSDocumentReaderTest
{

	/**
	 * Test method for {@link org.lemsml.jlems.core.api.LEMSDocumentReader#readModel(java.net.URL)}.
	 */
	@Test
	public void testReadModel()
	{
		LEMSDocumentReader reader = new LEMSDocumentReader();
		try
		{
			ILEMSDocument model = reader.readModel(this.getClass().getResource("/example1.xml"));
			assertNotNull(model);
		}
		catch(IOException e)
		{
			fail(e.getMessage());
		}
		catch(ContentError e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetLEMSRunConfiguration()
	{
		LEMSDocumentReader reader = new LEMSDocumentReader();
		try
		{
			ILEMSDocument model = reader.readModel(this.getClass().getResource("/example1.xml"));
			LEMSBuilder builder = new LEMSBuilder();
			builder.addDocument(model);
			ILEMSBuildOptions options = new LEMSBuildOptions();
			ILEMSBuildConfiguration config = new LEMSBuildConfiguration();
			options.addBuildOption(LEMSBuildOptionsEnum.FLATTEN);
			builder.build(config, options);
			ILEMSRunConfiguration runConfig = LEMSDocumentReader.getLEMSRunConfiguration(model);
			assertNotNull(model);
			assertNotNull(runConfig);
			Assert.assertEquals(0.08, runConfig.getRuntime(), 0);
			Assert.assertEquals(0.00001, runConfig.getTimestep(), 0);
			Assert.assertTrue(runConfig.getRecordedStates().size() == 3);
			Assert.assertEquals("p1[0]/tsince", runConfig.getRecordedStates().get(0).getState().getStatePath());
			Assert.assertEquals("p3[0]/v", runConfig.getRecordedStates().get(1).getState().getStatePath());
			Assert.assertEquals("hhpop[0]/v", runConfig.getRecordedStates().get(2).getState().getStatePath());

			//check we can simulate with what retrieved
			ILEMSSimulator simulator = new LEMSSimulator();
			ILEMSResultsContainer results = new LEMSResultsContainer();
			config = new LEMSBuildConfiguration(LEMSDocumentReader.getTarget(model));

			Collection<ILEMSStateInstance> stateInstances = builder.build(config, options);
			for(ILEMSStateInstance instance : stateInstances)
			{
				simulator.initialize(instance, runConfig);
				simulator.run(results);
				
				IStateIdentifier tsince=new StateIdentifier("p1[0]/tsince");
				IStateIdentifier p3v=new StateIdentifier("p3[0]/v");
				IStateIdentifier hhpopv=new StateIdentifier("hhpop[0]/v");
				assertNotNull(results.getStateValues(tsince));
				assertNotNull(results.getStateValues(p3v));
				assertNotNull(results.getStateValues(hhpopv));
			}
		}
		catch(IOException e)
		{
			fail(e.getMessage());
		}
		catch(ContentError e)
		{
			fail(e.getMessage());
		}
		catch(ParseError e)
		{
			fail(e.getMessage());
		}
		catch(LEMSBuildException e)
		{
			fail(e.getMessage());
		}
		catch(LEMSExecutionException e)
		{
			fail(e.getMessage());
		}
	}

	/*
	@Test
	public void testGetLEMSRunConfiguration2()
	{
		LEMSDocumentReader reader = new LEMSDocumentReader();
		try
		{
			ILEMSDocument model = reader.readModel(this.getClass().getResource("/LEMS_NML2_Ex5_DetCell.xml"));
			LEMSBuilder builder = new LEMSBuilder();
			builder.addDocument(model);
			ILEMSBuildOptions options = new LEMSBuildOptions();
			ILEMSBuildConfiguration config = new LEMSBuildConfiguration("sim1");
			options.addBuildOption(LEMSBuildOptionsEnum.FLATTEN);
			builder.build(config, options);
			ILEMSRunConfiguration runConfig = LEMSDocumentReader.getLEMSRunConfiguration(model);
			assertNotNull(model);
			assertNotNull(runConfig);
			Assert.assertEquals(0.3, runConfig.getRuntime(), 0);
			Assert.assertEquals(0.00001, runConfig.getTimestep(), 0);
			Assert.assertEquals("hhpop[0]/v", runConfig.getRecordedStates().get(0).getState().getStatePath());

		}
		catch(IOException e)
		{
			fail(e.getMessage());
		}
		catch(ContentError e)
		{
			fail(e.getMessage());
		}
		catch(ParseError e)
		{
			fail(e.getMessage());
		}
		catch(LEMSBuildException e)
		{
			fail(e.getMessage());
		}
	}*/

	
	@Test
	public void testGetTarget()
	{
		LEMSDocumentReader reader = new LEMSDocumentReader();
		try
		{
			ILEMSDocument model = reader.readModel(this.getClass().getResource("/example1.xml"));
			LEMSBuilder builder = new LEMSBuilder();
			builder.addDocument(model);
			ILEMSBuildOptions options = new LEMSBuildOptions();
			ILEMSBuildConfiguration config = new LEMSBuildConfiguration();
			options.addBuildOption(LEMSBuildOptionsEnum.FLATTEN);
			builder.build(config, options);
			Assert.assertEquals("net1", LEMSDocumentReader.getTarget(model));
		}
		catch(IOException e)
		{
			fail(e.getMessage());
		}
		catch(ContentError e)
		{
			fail(e.getMessage());
		}
		catch(ParseError e)
		{
			fail(e.getMessage());
		}
		catch(LEMSBuildException e)
		{
			fail(e.getMessage());
		}
	}

}

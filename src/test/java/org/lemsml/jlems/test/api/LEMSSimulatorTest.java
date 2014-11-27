/**
 * 
 */
package org.lemsml.jlems.test.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;
import org.lemsml.jlems.api.LEMSBuildConfiguration;
import org.lemsml.jlems.api.LEMSBuildException;
import org.lemsml.jlems.api.LEMSBuildOptions;
import org.lemsml.jlems.api.LEMSBuildOptionsEnum;
import org.lemsml.jlems.api.LEMSBuilder;
import org.lemsml.jlems.api.LEMSDocumentReader;
import org.lemsml.jlems.api.LEMSExecutionException;
import org.lemsml.jlems.api.LEMSResultsContainer;
import org.lemsml.jlems.api.LEMSRunConfiguration;
import org.lemsml.jlems.api.LEMSSimulator;
import org.lemsml.jlems.api.StateIdentifier;
import org.lemsml.jlems.api.StateRecord;
import org.lemsml.jlems.api.interfaces.ILEMSBuildConfiguration;
import org.lemsml.jlems.api.interfaces.ILEMSBuildOptions;
import org.lemsml.jlems.api.interfaces.ILEMSBuilder;
import org.lemsml.jlems.api.interfaces.ILEMSDocument;
import org.lemsml.jlems.api.interfaces.ILEMSDocumentReader;
import org.lemsml.jlems.api.interfaces.ILEMSResultsContainer;
import org.lemsml.jlems.api.interfaces.ILEMSRunConfiguration;
import org.lemsml.jlems.api.interfaces.ILEMSSimulator;
import org.lemsml.jlems.api.interfaces.ILEMSStateInstance;
import org.lemsml.jlems.api.interfaces.IStateIdentifier;
import org.lemsml.jlems.core.sim.ContentError;

/**
 * @author matteocantarelli
 * 
 */
public class LEMSSimulatorTest
{

	@Test
	public void testRunExample1()
	{
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
			for(ILEMSStateInstance instance : stateInstances)
			{
				ILEMSRunConfiguration runConfig = new LEMSRunConfiguration(0.00005d, 0.08d);

				IStateIdentifier tsince = new StateIdentifier("p1[0]/tsince");
				IStateIdentifier p3v = new StateIdentifier("p3[0]/v");
				IStateIdentifier hhpopv = new StateIdentifier("hhpop[0]/v");

				runConfig.addStateRecord(new StateRecord(tsince));
				runConfig.addStateRecord(new StateRecord(p3v));
				runConfig.addStateRecord(new StateRecord(hhpopv));

				ILEMSResultsContainer results = new LEMSResultsContainer();
				simulator.initialize(instance, runConfig);
				simulator.run(results);

				assertNotNull(results.getState(tsince));
				assertNotNull(results.getState(p3v));
				assertNotNull(results.getState(hhpopv));

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
		catch(LEMSBuildException e)
		{
			fail(e.getMessage());
		}
		catch(LEMSExecutionException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testRunExample2()
	{
		ILEMSDocumentReader reader = new LEMSDocumentReader();
		ILEMSBuilder builder = new LEMSBuilder();
		try
		{

			ILEMSDocument model = reader.readModel(this.getClass().getResource("/example2.xml"));
			assertNotNull(model);

			builder.addDocument(model);
			ILEMSBuildOptions options = new LEMSBuildOptions();
			options.addBuildOption(LEMSBuildOptionsEnum.FLATTEN);

			ILEMSBuildConfiguration config = new LEMSBuildConfiguration("net1");

			Collection<ILEMSStateInstance> stateInstances = builder.build(config, options);

			assertNotNull(stateInstances);
			assertFalse(stateInstances.isEmpty());

			ILEMSSimulator simulator = new LEMSSimulator();
			for(ILEMSStateInstance instance : stateInstances)
			{
				ILEMSRunConfiguration runConfig = new LEMSRunConfiguration(0.00004d, 0.08d);

				IStateIdentifier tsince = new StateIdentifier("p1[0]/tsince");
				IStateIdentifier p3v = new StateIdentifier("p3[0]/v");
				IStateIdentifier hhpopv = new StateIdentifier("hhpop[0]/v");

				runConfig.addStateRecord(new StateRecord(tsince));
				runConfig.addStateRecord(new StateRecord(p3v));
				runConfig.addStateRecord(new StateRecord(hhpopv));

				ILEMSResultsContainer results = new LEMSResultsContainer();

				simulator.initialize(instance, runConfig);
				simulator.run(results);

				assertNotNull(results.getState(tsince));
				assertNotNull(results.getState(p3v));
				assertNotNull(results.getState(hhpopv));
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
		catch(LEMSBuildException e)
		{
			fail(e.getMessage());
		}
		catch(LEMSExecutionException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testRunExample3()
	{
		ILEMSDocumentReader reader = new LEMSDocumentReader();
		ILEMSBuilder builder = new LEMSBuilder();
		try
		{

			ILEMSDocument model = reader.readModel(this.getClass().getResource("/example3.xml"));
			assertNotNull(model);

			builder.addDocument(model);
			ILEMSBuildOptions options = new LEMSBuildOptions();
			options.addBuildOption(LEMSBuildOptionsEnum.FLATTEN);

			ILEMSBuildConfiguration config = new LEMSBuildConfiguration("net1");

			Collection<ILEMSStateInstance> stateInstances = builder.build(config, options);

			assertNotNull(stateInstances);
			assertFalse(stateInstances.isEmpty());

			ILEMSSimulator simulator = new LEMSSimulator();
			for(ILEMSStateInstance instance : stateInstances)
			{
				ILEMSRunConfiguration runConfig = new LEMSRunConfiguration(0.00005d, 0.08d);

				IStateIdentifier p1tsince = new StateIdentifier("p1[0]/tsince");
				IStateIdentifier p2tsince = new StateIdentifier("p2[0]/tsince");
				IStateIdentifier p30v = new StateIdentifier("p3[0]/v");
				IStateIdentifier p31v = new StateIdentifier("p3[1]/v");

				runConfig.addStateRecord(new StateRecord(p1tsince));
				runConfig.addStateRecord(new StateRecord(p2tsince));
				runConfig.addStateRecord(new StateRecord(p30v));
				runConfig.addStateRecord(new StateRecord(p31v));

				ILEMSResultsContainer results = new LEMSResultsContainer();

				simulator.initialize(instance, runConfig);
				simulator.run(results);

				assertNotNull(results.getState(p1tsince));
				assertNotNull(results.getState(p2tsince));
				assertNotNull(results.getState(p30v));
				assertNotNull(results.getState(p31v));
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
		catch(LEMSBuildException e)
		{
			fail(e.getMessage());
		}
		catch(LEMSExecutionException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testRunExample4()
	{
		ILEMSDocumentReader reader = new LEMSDocumentReader();
		ILEMSBuilder builder = new LEMSBuilder();
		try
		{

			ILEMSDocument model = reader.readModel(this.getClass().getResource("/example4.xml"));
			assertNotNull(model);

			builder.addDocument(model);
			ILEMSBuildOptions options = new LEMSBuildOptions();
			options.addBuildOption(LEMSBuildOptionsEnum.FLATTEN);

			ILEMSBuildConfiguration config = new LEMSBuildConfiguration("kscell_1");

			Collection<ILEMSStateInstance> stateInstances = builder.build(config, options);

			assertNotNull(stateInstances);
			assertFalse(stateInstances.isEmpty());

			ILEMSSimulator simulator = new LEMSSimulator();
			for(ILEMSStateInstance instance : stateInstances)
			{
				ILEMSRunConfiguration runConfig = new LEMSRunConfiguration(0.00005d, 0.08d);

				IStateIdentifier v = new StateIdentifier("v");

				runConfig.addStateRecord(new StateRecord(v));

				ILEMSResultsContainer results = new LEMSResultsContainer();

				simulator.initialize(instance, runConfig);
				simulator.run(results);

				assertNotNull(results.getState(v));

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
		catch(LEMSBuildException e)
		{
			fail(e.getMessage());
		}
		catch(LEMSExecutionException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testRunExample5()
	{
		ILEMSDocumentReader reader = new LEMSDocumentReader();
		ILEMSBuilder builder = new LEMSBuilder();
		try
		{

			ILEMSDocument model = reader.readModel(this.getClass().getResource("/example5.xml"));
			assertNotNull(model);

			builder.addDocument(model);
			ILEMSBuildOptions options = new LEMSBuildOptions();
			options.addBuildOption(LEMSBuildOptionsEnum.FLATTEN);

			ILEMSBuildConfiguration config = new LEMSBuildConfiguration("kscell_1");

			Collection<ILEMSStateInstance> stateInstances = builder.build(config, options);

			assertNotNull(stateInstances);
			assertFalse(stateInstances.isEmpty());

			ILEMSSimulator simulator = new LEMSSimulator();
			for(ILEMSStateInstance instance : stateInstances)
			{
				ILEMSRunConfiguration runConfig = new LEMSRunConfiguration(0.00005d, 0.08d);

				IStateIdentifier v = new StateIdentifier("v");

				runConfig.addStateRecord(new StateRecord(v));

				ILEMSResultsContainer results = new LEMSResultsContainer();

				simulator.initialize(instance, runConfig);
				simulator.run(results);

				assertNotNull(results.getState(v));

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
		catch(LEMSBuildException e)
		{
			fail(e.getMessage());
		}
		catch(LEMSExecutionException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testRunExample6()
	{
		ILEMSDocumentReader reader = new LEMSDocumentReader();
		ILEMSBuilder builder = new LEMSBuilder();
		try
		{

			ILEMSDocument model = reader.readModel(this.getClass().getResource("/example6.xml"));
			assertNotNull(model);

			builder.addDocument(model);
			ILEMSBuildOptions options = new LEMSBuildOptions();

			ILEMSBuildConfiguration config = new LEMSBuildConfiguration("hhcell_1");

			Collection<ILEMSStateInstance> stateInstances = builder.build(config, options);

			assertNotNull(stateInstances);
			assertFalse(stateInstances.isEmpty());

			ILEMSSimulator simulator = new LEMSSimulator();
			for(ILEMSStateInstance instance : stateInstances)
			{
				ILEMSRunConfiguration runConfig = new LEMSRunConfiguration(0.00005d, 0.08d);

				IStateIdentifier v = new StateIdentifier("v");
				IStateIdentifier nageff = new StateIdentifier("NaPop/geff");
				IStateIdentifier kgeff = new StateIdentifier("KPop/geff");

				runConfig.addStateRecord(new StateRecord(v));
				runConfig.addStateRecord(new StateRecord(nageff));
				runConfig.addStateRecord(new StateRecord(kgeff));

				ILEMSResultsContainer results = new LEMSResultsContainer();

				simulator.initialize(instance, runConfig);
				simulator.run(results);

				assertNotNull(results.getState(v));
				assertNotNull(results.getState(nageff));
				assertNotNull(results.getState(kgeff));
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
		catch(LEMSBuildException e)
		{
			fail(e.getMessage());
		}
		catch(LEMSExecutionException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testRunExample7()
	{
		ILEMSDocumentReader reader = new LEMSDocumentReader();
		ILEMSBuilder builder = new LEMSBuilder();
		try
		{

			ILEMSDocument model = reader.readModel(this.getClass().getResource("/example7.xml"));
			assertNotNull(model);

			builder.addDocument(model);
			ILEMSBuildOptions options = new LEMSBuildOptions();
			options.addBuildOption(LEMSBuildOptionsEnum.FLATTEN);

			ILEMSBuildConfiguration config = new LEMSBuildConfiguration("net1");

			Collection<ILEMSStateInstance> stateInstances = builder.build(config, options);

			assertNotNull(stateInstances);
			assertFalse(stateInstances.isEmpty());

			ILEMSSimulator simulator = new LEMSSimulator();
			for(ILEMSStateInstance instance : stateInstances)
			{
				ILEMSRunConfiguration runConfig = new LEMSRunConfiguration(0.00005d, 0.08d);

				IStateIdentifier tsince = new StateIdentifier("p1[0]/tsince");
				IStateIdentifier p3v = new StateIdentifier("p3[0]/v");

				runConfig.addStateRecord(new StateRecord(tsince));
				runConfig.addStateRecord(new StateRecord(p3v));

				ILEMSResultsContainer results = new LEMSResultsContainer();

				simulator.initialize(instance, runConfig);
				simulator.run(results);

				assertNotNull(results.getState(tsince));
				assertNotNull(results.getState(p3v));
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
		catch(LEMSBuildException e)
		{
			fail(e.getMessage());
		}
		catch(LEMSExecutionException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	public void testRunExample8()
	{
		ILEMSDocumentReader reader = new LEMSDocumentReader();
		ILEMSBuilder builder = new LEMSBuilder();
		try
		{

			ILEMSDocument model = reader.readModel(this.getClass().getResource("/example8.xml"));
			assertNotNull(model);

			builder.addDocument(model);
			ILEMSBuildOptions options = new LEMSBuildOptions();
			options.addBuildOption(LEMSBuildOptionsEnum.FLATTEN);

			ILEMSBuildConfiguration config = new LEMSBuildConfiguration("net1");

			Collection<ILEMSStateInstance> stateInstances = builder.build(config, options);

			assertNotNull(stateInstances);
			assertFalse(stateInstances.isEmpty());

			ILEMSSimulator simulator = new LEMSSimulator();
			for(ILEMSStateInstance instance : stateInstances)
			{
				ILEMSRunConfiguration runConfig = new LEMSRunConfiguration(0.00005d, 0.08d);

				IStateIdentifier tsince = new StateIdentifier("p1[0]/tsince");
				IStateIdentifier p3v = new StateIdentifier("p3[0]/v");

				runConfig.addStateRecord(new StateRecord(tsince));
				runConfig.addStateRecord(new StateRecord(p3v));

				ILEMSResultsContainer results = new LEMSResultsContainer();

				simulator.initialize(instance, runConfig);
				simulator.run(results);

				assertNotNull(results.getState(tsince));
				assertNotNull(results.getState(p3v));
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
	public void testRunExampleNeuroML() throws ParseError
	{
		ILEMSDocumentReader reader = new LEMSDocumentReader();
		ILEMSBuilder builder = new LEMSBuilder();
		try
		{

			ILEMSDocument model = reader.readModel(this.getClass().getResource("/LEMS_NML2_Ex5_DetCell.xml"));
			assertNotNull(model);

			builder.addDocument(model);
			ILEMSBuildOptions options = new LEMSBuildOptions();

			ILEMSBuildConfiguration config = new LEMSBuildConfiguration("net1");

			Collection<ILEMSStateInstance> stateInstances = builder.build(config, options);

			assertNotNull(stateInstances);
			assertFalse(stateInstances.isEmpty());

			ILEMSSimulator simulator = new LEMSSimulator();
			for(ILEMSStateInstance instance : stateInstances)
			{
				ILEMSRunConfiguration runConfig = LEMSDocumentReader.getLEMSRunConfiguration(model);

				ILEMSResultsContainer results = new LEMSResultsContainer();

				simulator.initialize(instance, runConfig);
				simulator.run(results);

				// StandaloneViewer viewer=new StandaloneViewer();
				// double[] d={0.0, 80.0, -80.0, 50.0};
				// viewer.setRegion(d);
				// double x=0;
				// for(ALEMSValue value:results.getState(new StateIdentifier("hhpop[0]/v")))
				// {
				// LEMSDoubleValue dv=(LEMSDoubleValue) value;
				// viewer.addPoint("hhpop[0]/v", x*0.01, dv.getAsDouble()/0.001);
				// if(x<50)
				// {
				// System.out.println("X:"+x*0.01+" Y:"+dv.getAsDouble());
				// }
				// x++;
				// }
				// viewer.show();

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
		catch(LEMSBuildException e)
		{
			fail(e.getMessage());
		}
		catch(LEMSExecutionException e)
		{
			fail(e.getMessage());
		}
	}*/
}

/**
 * 
 */
package org.lemsml.jlems.test.api;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;
import org.lemsml.jlems.api.LEMSBuildConfiguration;
import org.lemsml.jlems.api.LEMSBuildException;
import org.lemsml.jlems.api.LEMSBuildOptions;
import org.lemsml.jlems.api.LEMSBuildOptionsEnum;
import org.lemsml.jlems.api.LEMSBuilder;
import org.lemsml.jlems.api.LEMSDocumentReader;
import org.lemsml.jlems.api.interfaces.ILEMSBuildConfiguration;
import org.lemsml.jlems.api.interfaces.ILEMSBuildOptions;
import org.lemsml.jlems.api.interfaces.ILEMSDocument;
import org.lemsml.jlems.api.interfaces.ILEMSStateInstance;
import org.lemsml.jlems.core.sim.ContentError;

/**
 * @author matteocantarelli
 *
 */
public class LEMSBuilderTest
{

	/**
	 * Test method for {@link org.lemsml.jlems.core.api.LEMSBuilder#build(org.lemsml.jlems.core.api.interfaces.ILEMSBuildOptions)}.
	 */
	@Test
	public void testBuild()
	{
		LEMSDocumentReader reader=new LEMSDocumentReader();
		LEMSBuilder builder=new LEMSBuilder();
		try
		{
			ILEMSDocument model=reader.readModel(this.getClass().getResource("/example1.xml"));
			assertNotNull(model);
			builder.addDocument(model);
			ILEMSBuildOptions options=new LEMSBuildOptions();
			ILEMSBuildConfiguration config = new LEMSBuildConfiguration("net1");
			options.addBuildOption(LEMSBuildOptionsEnum.FLATTEN);
			Collection<ILEMSStateInstance> stateInstance = builder.build(config,options);
			assertNotNull(stateInstance);
			assertFalse(stateInstance.isEmpty());
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

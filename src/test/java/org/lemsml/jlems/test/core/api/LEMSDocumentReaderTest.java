/**
 * 
 */
package org.lemsml.jlems.test.core.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.lemsml.jlems.core.api.LEMSDocumentReader;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Lems;

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
		LEMSDocumentReader reader=new LEMSDocumentReader();
		try
		{
			Lems model=reader.readModel(this.getClass().getResource("/example1.xml"));
			assertNotNull(model);
		}
		catch (IOException e)
		{
			fail(e.getMessage());
		}
		catch (ContentError e)
		{
			fail(e.getMessage());
		}
	}

}

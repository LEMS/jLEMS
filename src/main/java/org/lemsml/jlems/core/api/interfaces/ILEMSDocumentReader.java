/**
 * 
 */
package org.lemsml.jlems.core.api.interfaces;

import java.io.IOException;
import java.net.URL;

import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Lems;

/**
 * @author matteocantarelli
 *
 */
public interface ILEMSDocumentReader
{
	//reading models
	public Lems readModel(URL model) throws IOException, ContentError;
	
}

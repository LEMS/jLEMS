/**
 * 
 */
package org.lemsml.jlems.core.api.interfaces;

import java.net.URL;

/**
 * @author matteocantarelli
 *
 */
public interface ILEMSDocumentReader
{
	//reading models
	public ILEMSDocument readModel(URL model);
	
}

/**
 * 
 */
package org.lemsml.jlems.core.api.interfaces;

import java.net.URL;

import org.lemsml.jlems.model.LemsDoc;

/**
 * @author matteocantarelli
 *
 */
public interface ILEMSDocumentReader
{
	//reading models
	public LemsDoc readModel(URL model);
	
}

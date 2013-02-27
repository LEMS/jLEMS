/**
 * 
 */
package org.lemsml.jlems.core.api.interfaces;

import java.util.List;

import org.lemsml.jlems.core.api.LEMSBuildException;
import org.lemsml.jlems.model.LemsDoc;

/**
 * @author matteocantarelli
 *
 */
public interface ILEMSBuilder
{
	//init the simulator
	public void addDocument(List<LemsDoc> lemsDocument);
		
	public List<ILEMSState> createLEMSStates(ILEMSBuildOptions options) throws LEMSBuildException;
	
	public ILEMSInstance createExecutableInstances() throws LEMSBuildException;
	
	public ILEMSInstance build() throws LEMSBuildException; 
	
}

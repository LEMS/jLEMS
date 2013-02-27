/**
 * 
 */
package org.lemsml.jlems.core.api.interfaces;

import java.util.List;

import org.lemsml.jlems.core.api.LEMSBuildException;

/**
 * @author matteocantarelli
 *
 */
public interface ILEMSBuilder
{
	//init the simulator
	public void addDocument(List<ILEMSDocument> lemsDocument);
		
	public List<ILEMSState> createLEMSStates(ILEMSBuildOptions options) throws LEMSBuildException;
	
	public ILEMSInstance createExecutableInstances() throws LEMSBuildException;
	
	public ILEMSInstance build() throws LEMSBuildException; 
	
}

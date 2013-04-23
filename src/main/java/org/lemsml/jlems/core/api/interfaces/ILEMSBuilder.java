/**
 * 
 */
package org.lemsml.jlems.core.api.interfaces;

import java.util.Collection;
import java.util.Map;

import org.lemsml.jlems.core.api.LEMSBuildException;

/**
 * @author matteocantarelli
 *
 */
public interface ILEMSBuilder
{

	public void addDocument(ILEMSDocument lemsDocument);
		
	public Map<ILEMSStateType, ILEMSDocument> createLEMSStates(ILEMSBuildConfiguration config, ILEMSBuildOptions options) throws LEMSBuildException;
	
	public Collection<ILEMSStateInstance> createExecutableInstance(Map<ILEMSStateType, ILEMSDocument> stateMap, ILEMSBuildOptions options) throws LEMSBuildException;
	
	public Collection<ILEMSStateInstance> build(ILEMSBuildConfiguration config, ILEMSBuildOptions options) throws LEMSBuildException; 

	
}

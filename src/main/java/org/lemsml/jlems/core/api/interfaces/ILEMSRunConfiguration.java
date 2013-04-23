/**
 * 
 */
package org.lemsml.jlems.core.api.interfaces;

import java.util.Collection;

/**
 * @author matteocantarelli
 *
 */
public interface ILEMSRunConfiguration
{

	public double getTimestep();
	
	public double getRuntime();
	
	public void addStateRecord(IStateRecord stateRecord);
	
	public Collection<IStateRecord> getRecordedStates();
	
}

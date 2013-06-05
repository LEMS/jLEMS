/**
 * 
 */
package org.lemsml.jlems.core.api.interfaces;

import java.util.List;

/**
 * @author matteocantarelli
 *
 */
public interface ILEMSRunConfiguration
{

	public double getTimestep();
	
	public double getRuntime();
	
	public void addStateRecord(IStateRecord stateRecord);
	
	public List<IStateRecord> getRecordedStates();
	
}

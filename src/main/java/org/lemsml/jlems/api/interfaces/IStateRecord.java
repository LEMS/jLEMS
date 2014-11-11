package org.lemsml.jlems.api.interfaces;


public interface IStateRecord
{

	public int getStartRecordingTimeStep();
	
	public int getEndRecordingTimeStep();
	
	public IStateIdentifier getState();

	public boolean record(int t);
	
}

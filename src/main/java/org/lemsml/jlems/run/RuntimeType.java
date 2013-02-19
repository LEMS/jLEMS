package org.lemsml.jlems.run;

import org.lemsml.jlems.sim.ContentError;

public interface RuntimeType {

	String getID();

	
	public StateRunnable newStateRunnable() throws ContentError, ConnectionError, RuntimeError;
	
}

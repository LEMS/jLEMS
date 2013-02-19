package org.lemsml.jlems.run;

import java.util.HashMap;

import org.lemsml.jlems.sim.ContentError;


public abstract class ExecutableStatement {

	public abstract void execute(StateRunnable so, HashMap<String, StateRunnable> context) throws RuntimeError, ContentError, ConnectionError;
	 

}

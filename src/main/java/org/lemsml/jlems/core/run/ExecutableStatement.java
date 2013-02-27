package org.lemsml.jlems.core.run;

import java.util.HashMap;

import org.lemsml.jlems.core.sim.ContentError;


public abstract class ExecutableStatement {

	public abstract void execute(StateRunnable so, HashMap<String, StateRunnable> context) throws RuntimeError, ContentError, ConnectionError;
	 

}

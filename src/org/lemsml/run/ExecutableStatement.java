package org.lemsml.run;

import java.util.HashMap;

import org.lemsml.util.ContentError;
import org.lemsml.util.RuntimeError;

public abstract class ExecutableStatement {

	public abstract void execute(StateInstance so, HashMap<String, StateInstance> context) throws RuntimeError, ContentError, ConnectionError;
	 

}

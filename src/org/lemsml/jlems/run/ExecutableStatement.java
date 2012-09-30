package org.lemsml.jlems.run;

import java.util.HashMap;

import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.RuntimeError;

public abstract class ExecutableStatement {

	public abstract void execute(StateInstance so, HashMap<String, StateInstance> context) throws RuntimeError, ContentError, ConnectionError;
	 

}

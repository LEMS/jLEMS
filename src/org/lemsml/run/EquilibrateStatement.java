package org.lemsml.run;

import java.util.HashMap;

import org.lemsml.util.ContentError;
import org.lemsml.util.RuntimeError;

public class EquilibrateStatement extends ExecutableStatement {

	@Override
	public void execute(StateInstance so, HashMap<String, StateInstance> context) throws RuntimeError, ContentError {
		so.evaluate(null);
	}

}

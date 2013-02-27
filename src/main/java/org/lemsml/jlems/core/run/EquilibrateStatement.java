package org.lemsml.jlems.core.run;

import java.util.HashMap;

import org.lemsml.jlems.core.sim.ContentError;


public class EquilibrateStatement extends ExecutableStatement {

	@Override
	public void execute(StateRunnable so, HashMap<String, StateRunnable> context) throws RuntimeError, ContentError {
		so.evaluate(null);
	}

}

package org.lemsml.jlems.run;

import java.util.HashMap;

import org.lemsml.jlems.sim.ContentError;


public class EquilibrateStatement extends ExecutableStatement {

	@Override
	public void execute(StateRunnable so, HashMap<String, StateRunnable> context) throws RuntimeError, ContentError {
		so.evaluate(null);
	}

}

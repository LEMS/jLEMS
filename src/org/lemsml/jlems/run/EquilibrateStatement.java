package org.lemsml.jlems.run;

import java.util.HashMap;

import org.lemsml.jlems.sim.ContentError;


public class EquilibrateStatement extends ExecutableStatement {

	@Override
	public void execute(StateInstance so, HashMap<String, StateInstance> context) throws RuntimeError, ContentError {
		so.evaluate(null);
	}

}

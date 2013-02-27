package org.lemsml.jlems.core.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.core.sim.ContentError;


public class ExecutableProcedure {

	ArrayList<ExecutableStatement> statements = new ArrayList<ExecutableStatement>();
	
	
	
	public void execute(StateRunnable so) throws RuntimeError, ContentError, ConnectionError {
		HashMap<String, StateRunnable> context = new HashMap<String, StateRunnable>();
		for (ExecutableStatement es : statements) {
			es.execute(so, context);
		}
	}



	public void addStep(ExecutableStatement es) {
		statements.add(es);
	}

	
	
}

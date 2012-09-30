package org.lemsml.jlems.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.RuntimeError;

public class ExecutableProcedure {

	ArrayList<ExecutableStatement> statements = new ArrayList<ExecutableStatement>();
	
	
	
	public void execute(StateInstance so) throws RuntimeError, ContentError, ConnectionError {
		HashMap<String, StateInstance> context = new HashMap<String, StateInstance>();
		for (ExecutableStatement es : statements) {
			es.execute(so, context);
		}
	}



	public void addStep(ExecutableStatement es) {
		statements.add(es);
	}

	
	
}

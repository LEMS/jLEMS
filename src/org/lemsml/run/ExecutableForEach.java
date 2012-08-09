package org.lemsml.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.util.ContentError;
import org.lemsml.util.RuntimeError;

public class ExecutableForEach extends ExecutableStatement {

	String select;
	String as;
	
	ArrayList<ExecutableStatement> statements = new ArrayList<ExecutableStatement>();
	
	public ExecutableForEach(String sel, String a) {
		select = sel;
		as = a;
		 
	}


	@Override
	public void execute(StateInstance so, HashMap<String, StateInstance> context) throws RuntimeError, ContentError, ConnectionError {
		 for (StateInstance si : so.getPathInstances(select)) {
			 context.put(as, si);
			 for (ExecutableStatement es : statements) {
				 es.execute(si, context);
			 }
		 }
	}


	public void add(ExecutableStatement es) {
		statements.add(es);
		
	}

}

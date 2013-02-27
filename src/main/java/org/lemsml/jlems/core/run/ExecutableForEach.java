package org.lemsml.jlems.core.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.core.sim.ContentError;


public class ExecutableForEach extends ExecutableStatement {

	String select;
	String as;
	
	ArrayList<ExecutableStatement> statements = new ArrayList<ExecutableStatement>();
	
	public ExecutableForEach(String sel, String a) {
		super();
		select = sel;
		as = a;
		 
	}


	@Override
	public void execute(StateRunnable so, HashMap<String, StateRunnable> context) throws RuntimeError, ContentError, ConnectionError {
		 for (StateRunnable si : so.getPathInstances(select)) {
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

package org.lemsml.jlems.sim;

import java.util.ArrayList;

import org.lemsml.jlems.run.ComponentBehavior;
import org.lemsml.jlems.run.RuntimeOutput;
import org.lemsml.jlems.util.E;

public class OutputCollector implements ComponentBehaviorVisitor {

	ArrayList<RuntimeOutput> outputs;
	
	public OutputCollector(ArrayList<RuntimeOutput> al) {
		outputs = al;
	}

	@Override
	public void visit(ComponentBehavior cb) {
		ArrayList<RuntimeOutput> a = cb.getRuntimeOutputs();
		if (a != null) {
			outputs.addAll(a);
		}
		if (a.size() > 0) {
			// E.info("Added output " + a.get(0) + " from " + cb);
		}
 		
	}

	
	
}

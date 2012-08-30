package org.lemsml.sim;

import java.util.ArrayList;

import org.lemsml.run.ComponentBehavior;
import org.lemsml.run.RuntimeOutput;
import org.lemsml.util.E;

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
			E.info("Added output " + a.get(0) + " from " + cb);
		}
 		
	}

	
	
}

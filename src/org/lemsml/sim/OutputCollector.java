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
		E.missing();
		
	}

	
	
}

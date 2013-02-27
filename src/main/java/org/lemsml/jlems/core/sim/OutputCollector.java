package org.lemsml.jlems.core.sim;

import java.util.ArrayList;

import org.lemsml.jlems.core.run.RuntimeDisplay;
import org.lemsml.jlems.core.run.RuntimeOutput;
import org.lemsml.jlems.core.run.StateType;

public class OutputCollector implements StateTypeVisitor {

	ArrayList<RuntimeOutput> outputs;
	
	public OutputCollector(ArrayList<RuntimeOutput> al) {
		outputs = al;
	}

	@Override
	public void visit(StateType cb) {
		ArrayList<RuntimeOutput> a = cb.getRuntimeOutputs();
		if (a != null) {
			outputs.addAll(a);
		}
	 
		
 		
	}

	
	
}

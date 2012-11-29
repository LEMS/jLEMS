package org.lemsml.jlems.sim;

import java.util.ArrayList;

import org.lemsml.jlems.run.StateType;
import org.lemsml.jlems.run.RuntimeOutput;

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

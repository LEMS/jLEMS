package org.lemsml.jlems.sim;

import java.util.ArrayList;

import org.lemsml.jlems.run.StateType;
import org.lemsml.jlems.run.RuntimeDisplay;

public class OutputCollector implements StateTypeVisitor {

	ArrayList<RuntimeDisplay> outputs;
	
	public OutputCollector(ArrayList<RuntimeDisplay> al) {
		outputs = al;
	}

	@Override
	public void visit(StateType cb) {
		ArrayList<RuntimeDisplay> a = cb.getRuntimeDisplays();
		if (a != null) {
			outputs.addAll(a);
		}
	 
		
 		
	}

	
	
}

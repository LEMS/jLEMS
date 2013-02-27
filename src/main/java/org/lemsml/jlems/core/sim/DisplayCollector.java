package org.lemsml.jlems.core.sim;

import java.util.ArrayList;

import org.lemsml.jlems.core.run.RuntimeDisplay;
import org.lemsml.jlems.core.run.StateType;

public class DisplayCollector implements StateTypeVisitor {

	ArrayList<RuntimeDisplay> outputs;
	
	public DisplayCollector(ArrayList<RuntimeDisplay> al) {
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

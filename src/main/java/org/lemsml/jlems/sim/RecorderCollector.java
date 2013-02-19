package org.lemsml.jlems.sim;

import java.util.ArrayList;

import org.lemsml.jlems.run.StateType;
import org.lemsml.jlems.run.RuntimeRecorder;

public class RecorderCollector implements StateTypeVisitor {

	ArrayList<RuntimeRecorder> recorders;
	
	public RecorderCollector(ArrayList<RuntimeRecorder> al) {
		recorders = al;
	}

	@Override
	public void visit(StateType cb) {
		ArrayList<RuntimeRecorder> a = cb.getRuntimeRecorders();
		if (a != null && !a.isEmpty()) {
			recorders.addAll(a);
			// E.info("Added recorder " + a + " from " + cb);
		}
		
	}

	
	
}

package org.lemsml.jlems.core.sim;

import java.util.ArrayList;

import org.lemsml.jlems.core.run.RuntimeEventRecorder;
import org.lemsml.jlems.core.run.StateType;

public class EventRecorderCollector implements StateTypeVisitor {

	ArrayList<RuntimeEventRecorder> eventRecorders;
	
	public EventRecorderCollector(ArrayList<RuntimeEventRecorder> al) {
		eventRecorders = al;
	}

	@Override
	public void visit(StateType cb) {
		ArrayList<RuntimeEventRecorder> a = cb.getRuntimeEventRecorders();
		if (a != null && !a.isEmpty()) {
			eventRecorders.addAll(a);
			// E.info("Added recorder " + a + " from " + cb);
		}
		
	}

	
	
}

package org.lemsml.run;

import java.util.ArrayList;

import org.lemsml.util.RuntimeError;

public class EventManager {

	double time = 0;
	
	// TODO alternative class that uses a ring buffer
	
	
	ArrayList<PendingEvent> events = new ArrayList<PendingEvent>();
	
	
	public void advance(double t) throws RuntimeError {
		
		// TODO Auto-generated method stub
		int ntg = 0;
		for (PendingEvent pe : events) {
			if (pe.beforeOrEqual(t)) {
				pe.deliver();
		//		E.info("delivered event at " + pe.getTime() + " " + t);
				ntg += 1;
			} else {
				break;
			}
		}
		time = t;
		
		for (int i = 0; i < ntg; i++) {
			events.remove(0);
		}
	}

	public  void addEvent(InPort port, double delay) {
	// 	E.info("event manager adding event with delay " + delay);
		PendingEvent pe = new PendingEvent(port, time + delay);
		
		if (events.size() == 0) {
			events.add(pe);
		
		} else if (pe.after(events.get(events.size() - 1))) {
			events.add(pe);
		
		} else if (pe.before(events.get(0))) {
			events.add(0, pe);
			
		} else {
			insertEvent(pe);
		}	
	}
	
	private void insertEvent(PendingEvent pe) {
		int ia = 0;
		int ib = events.size() - 1;
		
		while (ib > ia + 1) {
			int ic = (ia + ib) / 2;
			PendingEvent ec = events.get(ic);
			if (pe.after(ec)) {
				ia = ic;
			} else {
				ib = ic;
			}
		}
		events.add(ib, pe);
	}

}

package org.lemsml.jlems.core.type.dynamics;

import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.EventPort;

public class EventOut {

	public String port;
	
	public EventPort r_eventPort;

	
	public void resolve(ComponentType base) throws ContentError {
		r_eventPort = base.getOutEventPort(port);
		
	}


	public String getPortName() {
		return r_eventPort.getName();
	}


	public EventOut makeCopy() {
		EventOut ret = new EventOut();
		ret.port = port;
		return ret;
	}
	
}

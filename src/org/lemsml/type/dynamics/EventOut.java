package org.lemsml.type.dynamics;

import org.lemsml.type.ComponentType;
import org.lemsml.type.EventPort;
import org.lemsml.util.ContentError;

public class EventOut {

	public String port;
	
	public EventPort r_eventPort;

	
	public void resolve(ComponentType base) throws ContentError {
		r_eventPort = base.getOutEventPort(port);
		
	}


	public String getPortName() {
		return r_eventPort.getName();
	}
	
}

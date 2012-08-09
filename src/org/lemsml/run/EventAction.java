package org.lemsml.run;
 
public class EventAction {

	String portName;
	
	ActionBlock action;
	
	
	public EventAction(String pn) {
		 portName = pn;
	}

	public String getPortName() {
		return portName;
	}
	
	public ActionBlock getAction() {
		return action;
	}


	public void setAction(ActionBlock ea) {
		action = ea; 
	}

	
}

package org.lemsml.jlems.core.run;

public class DelayRecipient {

	InPort port;
	double delay;
	
	EventManager eventManager;
	
	public DelayRecipient(InPort ip, double dly, EventManager em) {
		port = ip;
		delay = dly;
		eventManager = em;
	}

	public void send() {
		eventManager.addEvent(port, delay);
	}

	 
	
	
}

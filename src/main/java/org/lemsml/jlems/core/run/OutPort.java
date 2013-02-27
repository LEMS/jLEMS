package org.lemsml.jlems.core.run;

import java.util.ArrayList;

 
public class OutPort {

	public String name;
	
	
	ArrayList<InPort> recipients = new ArrayList<InPort>();
	
	ArrayList<DelayRecipient> delayRecipients = new ArrayList<DelayRecipient>();
	
	public OutPort(String s) {
		name = s;
	}
	
	public void connectTo(InPort ip) {
		recipients.add(ip);
	}
	
	
	public void connectTo(InPort ip, double delay, EventManager em) {
		if (delay <= 0) {
			connectTo(ip);
		
		} else {
			delayRecipients.add(new DelayRecipient(ip, delay, em));
		}
	}
	
	public void send() throws RuntimeError {
		for (InPort ip : recipients) {
			ip.receive();
		}
		for (DelayRecipient dr : delayRecipients) {
			dr.send();
		}
	}

}

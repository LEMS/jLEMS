package org.lemsml.jlems.core.run;

import java.util.ArrayList;
import org.lemsml.jlems.core.logging.E;

 
public class OutPort {

	public String name;
	
	
	ArrayList<InPortReceiver> recipients = new ArrayList<InPortReceiver>();
	
	ArrayList<DelayRecipient> delayRecipients = new ArrayList<DelayRecipient>();
	
	public OutPort(String s) {
		name = s;
	}
	
	public void connectTo(InPortReceiver ip) {
        //E.info("Connecting outport "+name+" to "+ip.getName());
		recipients.add(ip);
	}
	
	
	public void connectTo(InPortReceiver ip, double delay, EventManager em) {
		if (delay <= 0) {
			connectTo(ip);
		
		} else {
			delayRecipients.add(new DelayRecipient(ip, delay, em));
		}
	}
	
	public void send() throws RuntimeError {
		for (InPortReceiver ip : recipients) {
			ip.receive();
		}
		for (DelayRecipient dr : delayRecipients) {
			dr.send();
		}
	}

}

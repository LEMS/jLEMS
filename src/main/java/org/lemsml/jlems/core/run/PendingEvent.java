package org.lemsml.jlems.core.run;


public class PendingEvent {

	InPortReceiver port;
	double time;
	
	public PendingEvent(InPortReceiver ip, double t) {
		port = ip;
		time = t;
	}

	public boolean beforeOrEqual(double t) {
		return (time <= t);
	}
	
	public boolean before(PendingEvent pe) {
		return (time < pe.time);
	}
	
	public boolean after(PendingEvent pe) {
		return (time >= pe.time);
	}
	
	public void deliver() throws RuntimeError {
		port.receive();
	}

	public double getTime() {
		return time;
	}
	
}

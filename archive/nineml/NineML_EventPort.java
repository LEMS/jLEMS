package org.lemsml.jlems.nineml;

import org.lemsml.jlems.io.FormatException;
import org.lemsml.jlems.type.EventPort;

public class NineML_EventPort  {

	public String name;
	public String mode;
	
	
	 
	
	public EventPort getEventPort() throws FormatException {
		EventPort ep = new EventPort(name);
	
		if (mode.equals("send")) {
			ep.setDirection(EventPort.OUT);
		} else if (mode.equals("recv")) {
			ep.setDirection(EventPort.IN);
		} else {
			throw new FormatException("Unrecognized mode " + mode);
		}
		
		return ep;
	}
	
}

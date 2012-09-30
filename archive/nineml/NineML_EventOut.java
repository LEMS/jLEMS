package org.lemsml.jlems.nineml;

import org.lemsml.jlems.type.dynamics.EventOut;

public class NineML_EventOut   {

	public String port;

	 
	
	public EventOut getEventOut() {	
		EventOut eo = new EventOut();
		eo.port = port;
		return eo;
	}
	
	
	
	
}

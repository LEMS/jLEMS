package org.lemsml.jlems.core.type.simulation;

import org.lemsml.jlems.core.annotation.ModelProperty;
 

public class EventRecord {

	@ModelProperty(info="path for the component which will emit spikes to be recorded")
	public String quantity;
	
	
	public String eventPort;
    
 
    @Override
    public String toString() {
    	return "EventRecord q=" + quantity + " eventPort=" + eventPort;
    }
  
}

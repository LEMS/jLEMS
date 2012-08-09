package org.lemsml.type;

import org.lemsml.util.ContentError;

public class Target   {

	public String component;
	public Component r_component;
    
	public String reportFile;
	public String timesFile;
	
	
	public void resolve(Lems lems) throws ContentError {
		r_component = lems.getComponent(component);
	}
	
	public Component getComponent() {
		return r_component;
	}
}

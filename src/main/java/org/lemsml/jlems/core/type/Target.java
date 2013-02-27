package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;
import org.lemsml.jlems.core.sim.ContentError;

@ModelElement(info="A lems file can contain many component definitions. " +
		"A Target elements specifies that a components should be treated as the " +
		"entry point for simulation or other processing")
public class Target   {

	@ModelProperty(info="Reference to the entry point component")
	public String component;
	public Component r_component;
    
 	
	public void resolve(Lems lems) throws ContentError {
		r_component = lems.getComponent(component);
	}
	
	public Component getComponent() {
		return r_component;
	}

	public void setComponentID(String id) {
		component = id;
	}
}

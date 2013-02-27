package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.expression.Parser;
import org.lemsml.jlems.core.sim.ContentError;

@ModelElement(info="Specifies that a component can accept attached components of a particular class. Attached components can " +
		"be added at build time dependent on other events. For scoping " +
		"and access purposes they are like child components. The cannonical use of attachments is in " +
		"adding synapses to a cell when a network connection is made.")
public class Attachments implements Named {
	@ModelProperty(info="")
	public String name;
	@ModelProperty(info="")
	public String type;
	public ComponentType r_type;

	public String compClass; // TODO - the NineML name for type - should pre-process to type
	
    
	public void resolve(Lems lems, Parser p) throws ContentError, ParseError {
		LemsCollection<ComponentType> types = lems.getComponentTypes();
		if (type == null && compClass != null) {
			type = compClass;
		}
		ComponentType t = types.getByName(type);
		
		if (t != null) {
			r_type = t;
			r_type.checkResolve(lems, p);
	
			
			
		} else {
			throw new ContentError("no such class: " + type);
		}
		
	}


	public String getName() {
		 return name;
	}


	public ComponentType getComponentType() {
		return r_type;
	}

	
	
}

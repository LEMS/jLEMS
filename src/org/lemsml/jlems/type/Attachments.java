package org.lemsml.jlems.type;

import org.lemsml.jlems.annotation.Mat;
import org.lemsml.jlems.annotation.Mel;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.expression.Parser;
import org.lemsml.jlems.util.ContentError;

@Mel(info="Specifies that a component can accept attached components of a particular class. Attached components can " +
		"be added at build time dependent on other events. For scoping " +
		"and access purposes they are like child components. The cannonical use of attachments is in " +
		"adding synapses to a cell when a network connection is made.")
public class Attachments implements Named {
	@Mat(info="")
	public String name;
	@Mat(info="")
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

package org.lemsml.jlems.type.structure;

import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.run.BuilderElement;
import org.lemsml.jlems.run.CoBuilder;
import org.lemsml.jlems.run.ComponentBehavior;
import org.lemsml.jlems.type.Component;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.type.LemsCollection;
import org.lemsml.jlems.util.ContentError;

public class CoInstantiate extends BuildElement {

	
	public String at;
	 
	public String component;
	public Component r_component;
	
	public String componentType;
	ComponentType r_componentType;
	
	
	public LemsCollection<Assign> assigns = new LemsCollection<Assign>();
 
	@Override
	public void resolveLocal(Lems lems, ComponentType ct) throws ContentError, ParseError {
		if (componentType != null) {
			r_componentType = lems.getComponentTypeByName(componentType);
			
			r_component = new Component();
			r_component.setType(r_componentType);
			r_component.resolve(lems, null);
		}
	
	}
	
	
 
	public BuilderElement makeBuilder(Component cpt) throws ContentError, ParseError {
		ComponentBehavior cb = null;
		
		if (component != null) {
			Component c = cpt.getChild(component);
			cb = c.getComponentBehavior();
		} else if (component != null) {
			cb = r_component.getComponentBehavior();
		}
		

		Component c_ctr = cpt.getScopeComponent(at);
	 
		CoBuilder bdr = new CoBuilder(c_ctr, cb);
	 
		return bdr;
	}
	
}

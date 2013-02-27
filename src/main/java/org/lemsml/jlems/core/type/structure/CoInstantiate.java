package org.lemsml.jlems.core.type.structure;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.run.BuilderElement;
import org.lemsml.jlems.core.run.CoBuilder;
import org.lemsml.jlems.core.run.StateType;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.LemsCollection;

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
		StateType cb = null;
		
		if (component != null) {
			Component c = cpt.getChild(component);
			cb = c.getStateType();
			
		} else if (r_component != null) {
			cb = r_component.getStateType();
		}
		

		Component c_ctr = cpt.getScopeComponent(at);
	 
		CoBuilder bdr = new CoBuilder(c_ctr, cb);
	 
		return bdr;
	}
	
}

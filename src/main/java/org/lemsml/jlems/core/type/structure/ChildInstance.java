package org.lemsml.jlems.core.type.structure;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.run.BuilderElement;
import org.lemsml.jlems.core.run.RuntimeType;
import org.lemsml.jlems.core.run.SingleChildBuilder;
import org.lemsml.jlems.core.run.StateType;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.LemsCollection;

public class ChildInstance extends BuildElement {

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
            RuntimeType cb = null;

            if (component != null) {
                Component c = null;
                if (component.startsWith("../")) {
                    String newPath = component.substring(3);
                    c = cpt.getParent().getChild(newPath);
                } else {
                    c = cpt.getChild(component);
                }
                cb = c.getRuntimeType();

            } else if (component != null) {
                cb = r_component.getRuntimeType();
            }
	
		SingleChildBuilder sb = new SingleChildBuilder(component, cb);
	
		/*
		for (Assign ass : assigns) {
			mb.addAssignment(ass.getProperty(), ass.getExpression());
		}
		*/
		
		return sb;
	}
	
}

package org.lemsml.jlems.type.structure;

import org.lemsml.jlems.expression.DoubleEvaluable;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.run.BuilderElement;
import org.lemsml.jlems.run.ComponentBehavior;
import org.lemsml.jlems.run.MultiBuilder;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.type.Component;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.type.LemsCollection;
 

public class MultiInstantiate extends BuildElement {

	
	public String number;
	
	public String indexVariable = "index";
	
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
		for (Assign ass : assigns) {
			DoubleEvaluable de = lems.getParser().parseExpression(ass.getExpression());
			ass.setDoubleEvaluable(de);
		}
	}
	
	
 
	public BuilderElement makeBuilder(Component cpt) throws ContentError, ParseError {
		ComponentBehavior cb = null;
		
		if (component != null) {
			Component c = cpt.getChild(component);
			cb = c.getComponentBehavior();
			
		} else if (r_component != null) {
			cb = r_component.getComponentBehavior();
		}
		
		int n = (int)Math.round(cpt.getParamValue(number).getDoubleValue());
		
		MultiBuilder mb = new MultiBuilder(n, cb);
		
		mb.setIndexVariable(indexVariable);
		for (Assign ass : assigns) {
			String cea = null;
			String ea = ass.getExposeAs();
			if (ea != null) {
				cea = cpt.getTextParam(ea);
			}
			
			mb.addAssignment(ass.getProperty(), ass.getDoubleEvaluable(), cea);
		}
		
		return mb;
	}
	
}

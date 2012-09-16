package org.lemsml.type.dynamics;

import org.lemsml.expression.DoubleEvaluable;
import org.lemsml.expression.ParseError;
import org.lemsml.run.BuilderElement;
import org.lemsml.run.ComponentBehavior;
import org.lemsml.run.MultiBuilder;
import org.lemsml.type.Component;
import org.lemsml.type.ComponentType;
import org.lemsml.type.Lems;
import org.lemsml.type.LemsCollection;
import org.lemsml.util.ContentError;
 

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

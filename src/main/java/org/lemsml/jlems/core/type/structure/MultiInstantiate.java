package org.lemsml.jlems.core.type.structure;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.expression.ParseTree;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.BuilderElement;
import org.lemsml.jlems.core.run.MultiBuilder;
import org.lemsml.jlems.core.run.StateType;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.LemsCollection;
 

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
			ParseTree pt = lems.getParser().parseExpression(ass.getExpression());
			ass.setDoubleEvaluator(pt.makeFloatEvaluator());
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
		
		int n = (int)Math.round(cpt.getParamValue(number).getDoubleValue());
		
		MultiBuilder mb = new MultiBuilder(n, cb);
		
		mb.setIndexVariable(indexVariable);
		for (Assign ass : assigns) {
			String cea = null;
			String ea = ass.getExposeAs();
			if (ea != null) {
				cea = cpt.getTextParam(ea);
			}
			// TODO - assigns don't know thier dimenison?
			E.missing("Dont' know dimension in assigns");
			String dim = "unknown";
			
			mb.addAssignment(ass.getProperty(), ass.getDoubleEvaluator(), cea, dim);
		}
		
		return mb;
	}
	
}

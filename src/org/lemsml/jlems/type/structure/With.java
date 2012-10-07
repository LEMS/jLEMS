package org.lemsml.jlems.type.structure;

import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.run.BuilderElement;
import org.lemsml.jlems.run.WithBuilder;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.type.Component;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.type.PathEvaluator;

public class With extends BuildElement {

	public String instance;
	
	public String as;
	 
	public BuilderElement makeBuilder(Component cpt) throws ContentError, ParseError {
		
		PathEvaluator pe = new PathEvaluator(null, cpt, instance);
	 	String insval = pe.getStringValue();	
	 		
		BuilderElement ret = new WithBuilder(insval, as);
		super.makeChildBuilders(cpt, ret);
		return ret;
	}

	@Override
	public void resolveLocal(Lems lems, ComponentType ct) throws ContentError, ParseError {
		// TODO Auto-generated method stub
		
	}
	
}

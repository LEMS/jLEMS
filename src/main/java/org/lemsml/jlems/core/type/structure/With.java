package org.lemsml.jlems.core.type.structure;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.run.BuilderElement;
import org.lemsml.jlems.core.run.WithBuilder;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.PathEvaluator;

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

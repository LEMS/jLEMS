package org.lemsml.jlems.core.type.structure;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.run.BuilderElement;
import org.lemsml.jlems.core.run.ForEachBuilder;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.PathEvaluator;
 

public class ForEach extends BuildElement {

	public String instances;
	
	public String as;

	 
	public BuilderElement makeBuilder(Component cpt) throws ContentError, ParseError {
		
        //E.info("ForEach makeBuilder on: " + cpt+", instances: "+instances+", as: "+ as);
		PathEvaluator pe = new PathEvaluator(null, cpt, instances);
		
		String insval = pe.getStringValue();	
		
		
		// E.info("ForEeach instances value in builder: " + insval);
		
		
		BuilderElement ret = new ForEachBuilder(insval, as);
		super.makeChildBuilders(cpt, ret);
		return ret;
	}


	@Override
	public void resolveLocal(Lems lems, ComponentType ct) throws ContentError, ParseError {
	 	// nothing to do
	}
	
}

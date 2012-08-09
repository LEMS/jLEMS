package org.lemsml.behavior;

import org.lemsml.expression.ParseError;
import org.lemsml.run.BuilderElement;
import org.lemsml.run.ForEachBuilder;
import org.lemsml.type.Component;
import org.lemsml.type.PathEvaluator;
import org.lemsml.util.ContentError;
 

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
	
}

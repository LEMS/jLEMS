package org.lemsml.behavior;

import org.lemsml.expression.ParseError;
import org.lemsml.run.BuilderElement;
import org.lemsml.run.WithBuilder;
import org.lemsml.type.Component;
import org.lemsml.type.PathEvaluator;
import org.lemsml.util.ContentError;

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
	
}

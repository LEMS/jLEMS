package org.lemsml.jlems.core.type.structure;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.run.BuilderElement;
import org.lemsml.jlems.core.run.WithBuilder;
import org.lemsml.jlems.core.run.WithListItemBuilder;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.IndexParameter;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.PathEvaluator;

public class With extends BuildElement {

	public String instance;
	
	public String list;
	
	public String index;
	private IndexParameter r_indexParameter;
	
	public String as;
	 
	public BuilderElement makeBuilder(Component cpt) throws ContentError, ParseError {
		
		BuilderElement ret = null;
		
		if (instance != null) {
		PathEvaluator pe = new PathEvaluator(null, cpt, instance);
	 	String insval = pe.getStringValue();	
			ret = new WithBuilder(insval, as);
			super.makeChildBuilders(cpt, ret);
	 		
		} else if (list != null && index != null) {
			 
			//  should 
			PathEvaluator pe = new PathEvaluator(null, cpt, list);
			String listval = pe.getRelativeStringValue();	
			
			
			PathEvaluator pei = new PathEvaluator(null, cpt, index);
			String idxval = pei.getStringValue();	
			int idx = Integer.parseInt(idxval);
			ret = new WithListItemBuilder(listval, idx, as);
			
		} else {
			throw new ContentError("With must specify 'instance' or both 'index' and 'list'");
		}
		return ret;
	}

	
	
	@Override
	public void resolveLocal(Lems lems, ComponentType ct) throws ContentError, ParseError {
		
		if (index != null) {
			r_indexParameter = ct.getIndexParameter(index);
        }
	
		if (list != null) {
			// TODO
			// should check there is a Component or ComponentRequirement at the 
			// path specified by list
        }
		
	}
	
}

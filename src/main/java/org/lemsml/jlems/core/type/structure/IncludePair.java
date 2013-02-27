package org.lemsml.jlems.core.type.structure;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.BuilderElement;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.PairCollection;

public class IncludePair extends BuildElement {


	
	public String collection;
	public PairCollection r_collection;
		
	
	public BuilderElement makeBuilder(Component cpt) throws ContentError, ParseError {
		E.missing();
		return null;
	}

	@Override
	public void resolveLocal(Lems lems, ComponentType ct) throws ContentError, ParseError {
		r_collection = ct.getPairCollection(collection);
	 	
		if (r_collection == null) {
			throw new ContentError("collection " + collection + " not found in " + ct);
		} 
		
		
	
	}

 
	
}

package org.lemsml.behavior;

import org.lemsml.expression.ParseError;
import org.lemsml.run.BuilderElement;
import org.lemsml.type.Component;
import org.lemsml.type.ComponentType;
import org.lemsml.type.Lems;
import org.lemsml.type.PairCollection;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;

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

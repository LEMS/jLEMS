package org.lemsml.jlems.type.structure;

import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.run.BuilderElement;
import org.lemsml.jlems.type.Component;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.type.PairCollection;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.E;

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

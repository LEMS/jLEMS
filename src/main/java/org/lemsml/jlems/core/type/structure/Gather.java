package org.lemsml.jlems.core.type.structure;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.BuilderElement;
import org.lemsml.jlems.core.run.GatherBuilder;
import org.lemsml.jlems.core.selection.SelectionExpression;
import org.lemsml.jlems.core.selection.SelectionParser;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Collection;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;
 

public class Gather extends BuildElement {

	public String pathParameter;
 
	public String collection;
	public Collection r_collection;
	
	
	
	public BuilderElement makeBuilder(Component cpt) throws ContentError, ParseError {
		String sel = cpt.getPathParameterPath(pathParameter);
		SelectionParser sp = new SelectionParser();
		SelectionExpression selexp = sp.parse(sel);
 		
		return new GatherBuilder(selexp, collection);
		
	}

	@Override
	public void resolveLocal(Lems lems, ComponentType ct) throws ContentError, ParseError {
		r_collection = ct.getCollection(collection);
	 	
		if (r_collection == null) {
			throw new ContentError("collection " + collection + " not found in " + ct);
		} 
		
		
	
	}
	
	
}

package org.lemsml.behavior;

import org.lemsml.expression.ParseError;
import org.lemsml.run.BuilderElement;
import org.lemsml.run.GatherBuilder;
import org.lemsml.selection.SelectionExpression;
import org.lemsml.selection.SelectionParser;
import org.lemsml.type.Collection;
import org.lemsml.type.Component;
import org.lemsml.type.ComponentType;
import org.lemsml.type.Lems;
import org.lemsml.util.ContentError;

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

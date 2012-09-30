package org.lemsml.jlems.type.structure;

import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.run.BuilderElement;
import org.lemsml.jlems.run.GatherBuilder;
import org.lemsml.jlems.selection.SelectionExpression;
import org.lemsml.jlems.selection.SelectionParser;
import org.lemsml.jlems.type.Collection;
import org.lemsml.jlems.type.Component;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.util.ContentError;

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

package org.lemsml.type.dynamics;

import org.lemsml.expression.ParseError;
import org.lemsml.run.BuilderElement;
import org.lemsml.run.GatherPairsBuilder;
import org.lemsml.selection.SelectionExpression;
import org.lemsml.selection.SelectionParser;
import org.lemsml.type.Component;
import org.lemsml.type.ComponentType;
import org.lemsml.type.Lems;
import org.lemsml.type.PairCollection;
import org.lemsml.util.ContentError;

public class GatherPairs extends BuildElement {

	public String id;
	 
	public String pFrom;
	public String qFrom;
	
	public String collection;
	public PairCollection r_collection;
 	 
	
	
	public BuilderElement makeBuilder(Component cpt) throws ContentError, ParseError {
		String psel = cpt.getPathParameterPath(pFrom);
		SelectionParser sp = new SelectionParser();
		SelectionExpression pselexp = sp.parse(psel);
		
		String qsel = cpt.getPathParameterPath(qFrom);
		SelectionExpression qselexp = sp.parse(qsel);
	
		GatherPairsBuilder ret = new GatherPairsBuilder(pselexp, qselexp, collection);
		
		// TODO shouldn't need to put this in explicitly
		super.makeChildBuilders(cpt, ret);
		return ret;
	}

	@Override
	public void resolveLocal(Lems lems, ComponentType ct) throws ContentError, ParseError {
		r_collection = ct.getPairCollection(collection);
	 	
		if (r_collection == null) {
			throw new ContentError("collection " + collection + " not found in " + ct);
		} 
		
		
	
	}
	
	
}

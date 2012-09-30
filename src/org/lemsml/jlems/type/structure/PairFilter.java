package org.lemsml.jlems.type.structure;
 
import java.util.HashMap;

import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.run.BuilderElement;
import org.lemsml.jlems.run.PairFilterBuilder;
import org.lemsml.jlems.selection.SelectionExpression;
import org.lemsml.jlems.selection.SelectionParser;
import org.lemsml.jlems.type.Component;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.util.ContentError;

public class PairFilter extends BuildElement {

	public String select;
	
	public BuilderElement makeBuilder(Component cpt) throws ContentError, ParseError {
		
		HashMap<String, String> textMap = cpt.getTextParamMap();
		
		SelectionParser sp = new SelectionParser();
		SelectionExpression wexp = sp.parse(select);

		wexp.replaceSymbols(textMap);
		
		
		PairFilterBuilder ret = new PairFilterBuilder(wexp);
		return ret;
		
	}

	@Override
	public void resolveLocal(Lems lems, ComponentType ct) throws ContentError, ParseError {
		// TODO Auto-generated method stub
		
	}
}

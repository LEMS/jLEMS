package org.lemsml.jlems.core.type.structure;
 
import java.util.HashMap;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.run.BuilderElement;
import org.lemsml.jlems.core.run.PairFilterBuilder;
import org.lemsml.jlems.core.selection.SelectionExpression;
import org.lemsml.jlems.core.selection.SelectionParser;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;

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

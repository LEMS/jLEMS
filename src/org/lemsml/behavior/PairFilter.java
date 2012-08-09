package org.lemsml.behavior;
 
import java.util.HashMap;

import org.lemsml.expression.ParseError;
import org.lemsml.run.BuilderElement;
import org.lemsml.run.PairFilterBuilder;
import org.lemsml.selection.SelectionExpression;
import org.lemsml.selection.SelectionParser;
import org.lemsml.type.Component;
import org.lemsml.util.ContentError;

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
}

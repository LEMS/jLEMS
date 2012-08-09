package org.lemsml.run;

import java.util.ArrayList;

import org.lemsml.selection.SelectionExpression;
import org.lemsml.util.ContentError;

public class InstanceCollector {
 
	SelectionExpression selexpr;

	 

	public InstanceCollector(SelectionExpression ex) {
		selexpr = ex;
	}

	public ArrayList<StateInstance> collect(StateInstance root) throws ContentError, ConnectionError {
		ArrayList<StateInstance> ret = selexpr.getMatches(root) ; 
		return ret;
	}

}

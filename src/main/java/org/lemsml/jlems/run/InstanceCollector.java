package org.lemsml.jlems.run;

import java.util.ArrayList;

import org.lemsml.jlems.selection.SelectionExpression;
import org.lemsml.jlems.sim.ContentError;

public class InstanceCollector {
 
	SelectionExpression selexpr;

	 

	public InstanceCollector(SelectionExpression ex) {
		selexpr = ex;
	}

	public ArrayList<StateRunnable> collect(StateRunnable root) throws ContentError, ConnectionError, RuntimeError {
		ArrayList<StateRunnable> ret = selexpr.getMatches(root) ; 
		return ret;
	}

}

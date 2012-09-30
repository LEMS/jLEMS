package org.lemsml.jlems.selection;

import java.util.ArrayList;

import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.run.StateInstance;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.RuntimeError;

public class ApplyPredicateNode extends SelectionOperatorNode {

	double fpos;
	
	
	
	public ApplyPredicateNode(String s) {
		super(s);
	}

	 
 
	
	 
	@Override
	public SelectionOperatorNode copy() {
		return new ApplyPredicateNode(symbol);
	}

	 
	public String getEvaluationProcessDescription() {
		 return "(Apply predicate to " + left + ": " + right + ")";
	}

	
	
	@Override
	public ArrayList<StateInstance> getMatches(StateInstance baseSI) throws ContentError, ConnectionError, RuntimeError {
		ArrayList<StateInstance> ret = null;
		if (left instanceof SelectionNode && right instanceof PredicateNode) {
			ArrayList<StateInstance> ml = ((SelectionNode)left).getMatches(baseSI);
		 
			PredicateNode pnode = (PredicateNode)right;
			ret = new ArrayList<StateInstance>();
			for (StateInstance si : ml) {
				if (pnode.evaluate(si)) {
					ret.add(si);
				}
			}
			
		} else {
			throw new RuntimeException("Union applied to non-selections? " + left + " and " + right);
		}
		return ret;
	}
	
	
	
	
}

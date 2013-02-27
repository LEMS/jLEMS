package org.lemsml.jlems.core.selection;

import java.util.ArrayList;

import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.run.StateInstance;
import org.lemsml.jlems.core.run.StateRunnable;
import org.lemsml.jlems.core.sim.ContentError;

public class ApplyPredicateNode extends AbstractSelectionOperatorNode {

	double fpos;
	
	
	
	public ApplyPredicateNode(String s) {
		super(s);
	}

	 
 
	
	 
	@Override
	public AbstractSelectionOperatorNode copy() {
		return new ApplyPredicateNode(symbol);
	}

	 
	public String getEvaluationProcessDescription() {
		 return "(Apply predicate to " + left + ": " + right + ")";
	}

	
	
	@Override
	public ArrayList<StateRunnable> getMatches(StateRunnable baseSI) throws ContentError, ConnectionError, RuntimeError {
		ArrayList<StateRunnable> ret = null;
		if (left instanceof AbstractSelectionNode && right instanceof PredicateNode) {
			ArrayList<StateRunnable> ml = ((AbstractSelectionNode)left).getMatches(baseSI);
		 
			PredicateNode pnode = (PredicateNode)right;
			ret = new ArrayList<StateRunnable>();
			for (StateRunnable si : ml) {
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

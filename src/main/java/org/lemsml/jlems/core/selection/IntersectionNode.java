package org.lemsml.jlems.core.selection;

import java.util.ArrayList;

import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.run.StateInstance;
import org.lemsml.jlems.core.run.StateRunnable;
import org.lemsml.jlems.core.sim.ContentError;

public class IntersectionNode extends AbstractSelectionOperatorNode {

	public IntersectionNode(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

 

	@Override
	public double getPrecedence() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public AbstractSelectionOperatorNode copy() {
		return new IntersectionNode(symbol);
	}

	 
	public String getEvaluationProcessDescription() {
		return "(Intersection of " + left + " of " + right + ")";
	}
	
	
	@Override
	public ArrayList<StateRunnable> getMatches(StateRunnable baseSI) throws ContentError, ConnectionError, RuntimeError {
		ArrayList<StateRunnable> ret = null;
		if (left instanceof AbstractSelectionNode && right instanceof AbstractSelectionNode) {
			ArrayList<StateRunnable> ml = ((AbstractSelectionNode)left).getMatches(baseSI);
			ArrayList<StateRunnable> mr = ((AbstractSelectionNode)right).getMatches(baseSI);
			
			ret = new ArrayList<StateRunnable>();
			for (StateRunnable si : ml) {
				if (mr.contains(si)) {
					ret.add(si);
				}
			}
			
			
		} else {
			throw new RuntimeException("Union applied to non-selections? " + left + " and " + right);
		}
		return ret;
	}
	
	
	
}

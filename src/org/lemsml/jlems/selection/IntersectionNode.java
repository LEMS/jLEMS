package org.lemsml.jlems.selection;

import java.util.ArrayList;

import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.run.RuntimeError;
import org.lemsml.jlems.run.StateInstance;
import org.lemsml.jlems.sim.ContentError;

public class IntersectionNode extends SelectionOperatorNode {

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
	public SelectionOperatorNode copy() {
		return new IntersectionNode(symbol);
	}

	 
	public String getEvaluationProcessDescription() {
		return "(Intersection of " + left + " of " + right + ")";
	}
	
	
	@Override
	public ArrayList<StateInstance> getMatches(StateInstance baseSI) throws ContentError, ConnectionError, RuntimeError {
		ArrayList<StateInstance> ret = null;
		if (left instanceof SelectionNode && right instanceof SelectionNode) {
			ArrayList<StateInstance> ml = ((SelectionNode)left).getMatches(baseSI);
			ArrayList<StateInstance> mr = ((SelectionNode)right).getMatches(baseSI);
			
			ret = new ArrayList<StateInstance>();
			for (StateInstance si : ml) {
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

package org.lemsml.jlems.selection;

import java.util.ArrayList;

import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.run.RuntimeError;
import org.lemsml.jlems.run.StateInstance;
import org.lemsml.jlems.sim.ContentError;

public class ComplementNode extends SelectionOperatorNode {

	public ComplementNode(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

 
	@Override
	public double getPrecedence() {
		// TODO Auto-generated method stub
		return 11;
	}

	@Override
	public SelectionOperatorNode copy() {
		return new ComplementNode(symbol);
	}

	 
	public String getEvaluationProcessDescription() {
		 return "(Complement in " + left + " of " + right + ")";
	}

	
	@Override
	public ArrayList<StateInstance> getMatches(StateInstance baseSI) throws ContentError, ConnectionError, RuntimeError {
		ArrayList<StateInstance> ret = null;
		if (left instanceof SelectionNode && right instanceof SelectionNode) {
			ArrayList<StateInstance> ml = ((SelectionNode)left).getMatches(baseSI);
			ArrayList<StateInstance> mr = ((SelectionNode)right).getMatches(baseSI);
			
			ret = new ArrayList<StateInstance>();
			ret.addAll(ml);
			ret.removeAll(mr);
			
		} else {
			throw new RuntimeException("Union applied to non-selections? " + left + " and " + right);
		}
		return ret;
	}
	
	
	
	
}

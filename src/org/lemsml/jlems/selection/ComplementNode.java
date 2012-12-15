package org.lemsml.jlems.selection;

import java.util.ArrayList;

import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.run.RuntimeError;
import org.lemsml.jlems.run.StateInstance;
import org.lemsml.jlems.run.StateRunnable;
import org.lemsml.jlems.sim.ContentError;

public class ComplementNode extends AbstractSelectionOperatorNode {

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
	public AbstractSelectionOperatorNode copy() {
		return new ComplementNode(symbol);
	}

	 
	public String getEvaluationProcessDescription() {
		 return "(Complement in " + left + " of " + right + ")";
	}

	
	@Override
	public ArrayList<StateRunnable> getMatches(StateRunnable baseSI) throws ContentError, ConnectionError, RuntimeError {
		ArrayList<StateRunnable> ret = null;
		if (left instanceof AbstractSelectionNode && right instanceof AbstractSelectionNode) {
			ArrayList<StateRunnable> ml = ((AbstractSelectionNode)left).getMatches(baseSI);
			ArrayList<StateRunnable> mr = ((AbstractSelectionNode)right).getMatches(baseSI);
			
			ret = new ArrayList<StateRunnable>();
			ret.addAll(ml);
			ret.removeAll(mr);
			
		} else {
			throw new RuntimeException("Union applied to non-selections? " + left + " and " + right);
		}
		return ret;
	}
	
	
	
	
}

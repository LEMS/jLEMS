package org.lemsml.jlems.core.selection;

import java.util.ArrayList;

import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.run.StateInstance;
import org.lemsml.jlems.core.run.StateRunnable;
import org.lemsml.jlems.core.sim.ContentError;

public class UnionNode extends AbstractSelectionOperatorNode {

	public UnionNode(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	 

	@Override
	public double getPrecedence() {
		return 12;
	}

	@Override
	public AbstractSelectionOperatorNode copy() {
		return new UnionNode(symbol);
	}

 
	public String getEvaluationProcessDescription() {
		return "(Union of: " + left + " and " + right + ")";
	}

	@Override
	public ArrayList<StateRunnable> getMatches(StateRunnable baseSI) throws ContentError, ConnectionError, RuntimeError {
		ArrayList<StateRunnable> ret = null;
		if (left instanceof AbstractSelectionNode && right instanceof AbstractSelectionNode) {
			ArrayList<StateRunnable> ml = ((AbstractSelectionNode)left).getMatches(baseSI);
			ArrayList<StateRunnable> mr = ((AbstractSelectionNode)right).getMatches(baseSI);
			
			ret = new ArrayList<StateRunnable>();
			ret.addAll(ml);
			ret.addAll(mr);
			
		} else {
			throw new RuntimeException("Union applied to non-selections? " + left + " and " + right);
		}
		return ret;
	}

}

package org.lemsml.jlems.selection;

import java.util.ArrayList;

import org.lemsml.jlems.expression.Node;
import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.run.RuntimeError;
import org.lemsml.jlems.run.StateInstance;
import org.lemsml.jlems.sim.ContentError;

public class ColonNode extends SelectionOperatorNode {

 
	 
	public ColonNode() {
		super(":");
	}

	public ColonNode copy() {
		return new ColonNode();
	}
	
 
	public String getEvaluationProcessDescription() {
		 return "(Quantity selection in " + left + " select:  " + right + ")";
	}
 
	@Override
	public ArrayList<StateInstance> getMatches(StateInstance baseSI) throws ContentError, ConnectionError {
		ArrayList<StateInstance> ret = null;
		
		E.error("shouldn't be asking for matches on colon node " + this);
		/*
		E.info("slash node get matches: left=" + left + " right=" + right);
		
		Node nl = getLeft();
		if (nl instanceof SelectionNode) {
			ArrayList<StateInstance> wka = ((SelectionNode)nl).getMatches(baseSI);
			Node nr = getRight();
			if (nr instanceof  SelectionNode) {
				ret = new ArrayList<StateInstance>();
				SelectionNode rsn = (SelectionNode)nr;
				for (StateInstance si : wka) {
					ret.addAll(rsn.getMatches(si));
				}
				
			} else {
				throw new ContentError("cant subselect for: " + nr);
			}
			
		} else {
			throw new ContentError("cant get matches for: " + nl + " relative to " + baseSI);
		}
		*/
		return ret;
	}




	public double evaluateFloat(StateInstance si) throws ContentError, ConnectionError, RuntimeError {
		double ret = 0;
		boolean ok = false;
		Node nl = getLeft();
		Node nr = getRight();
		
		if (nr instanceof SelectorNode) {
			if (nl instanceof SelectionNode) {
				ArrayList<StateInstance> msia = ((SelectionNode)nl).getMatches(si);
				if (msia.size() == 1) {
					ret = ((SelectorNode)nr).getFloat(msia.get(0));
					ok = true;
				} else {
					throw new ContentError("need exactly one match from : " + nl + " but got " + msia.size());
				}
			}
			
		}
		
		if (!ok) {
			throw new ContentError("cant get a float from : " + this + " on " + si);
		}
		return ret;
	}
}

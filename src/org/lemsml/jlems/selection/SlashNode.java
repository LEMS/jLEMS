package org.lemsml.jlems.selection;

import java.util.ArrayList;

import org.lemsml.jlems.expression.Node;
import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.run.StateInstance;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.E;
import org.lemsml.jlems.util.RuntimeError;

public class SlashNode extends SelectionOperatorNode {

 
	 
	public SlashNode() {
		super("/");
	}

	public SlashNode copy() {
		return new SlashNode();
	}
	
 
	 
	public String getEvaluationProcessDescription() {
		 return "(Child selection in " + left + " select:  " + right + ")";
	}
 
	@Override
	public ArrayList<StateInstance> getMatches(StateInstance baseSI) throws ContentError, ConnectionError, RuntimeError {
		ArrayList<StateInstance> ret = null;
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
		
		return ret;
	}




	public double evaluateFloat(StateInstance si) throws ContentError, ConnectionError, RuntimeError {
		double ret = 0;
		boolean ok = false;
		Node nl = getLeft();
		Node nr = getRight();
		
		if (nr instanceof SelectorNode) {
			if (nl instanceof SelectionNode) {
				ArrayList<StateInstance> msia = ((SelectionNode)nr).getMatches(si);
				if (msia.size() == 1) {
					ret = ((SelectorNode)nr).getFloat(msia.get(0));
					
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

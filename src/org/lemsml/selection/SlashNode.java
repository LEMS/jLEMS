package org.lemsml.selection;

import java.util.ArrayList;

import org.lemsml.expression.Node;
import org.lemsml.run.ConnectionError;
import org.lemsml.run.StateInstance;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;

public class SlashNode extends SelectionOperatorNode {

 
	 
	public SlashNode() {
		super("/");
	}

	public SlashNode copy() {
		return new SlashNode();
	}
	

	@Override
	protected String getMathMLElementName() {
		// TODO Auto-generated method stub
		return null;
	}
	 
	public String getEvaluationProcessDescription() {
		 return "(Child selection in " + left + " select:  " + right + ")";
	}
 
	@Override
	public ArrayList<StateInstance> getMatches(StateInstance baseSI) throws ContentError, ConnectionError {
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




	public double evaluateFloat(StateInstance si) throws ContentError, ConnectionError {
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

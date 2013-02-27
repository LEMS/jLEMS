package org.lemsml.jlems.core.selection;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.run.StateInstance;
import org.lemsml.jlems.core.run.StateRunnable;
import org.lemsml.jlems.core.sim.ContentError;

public class SelectionExpression {

	String src;
	AbstractSelectionNode root;
	
	public SelectionExpression(String s, AbstractSelectionNode rn) {
		src = s;
		root = rn;
	}
	
	public String toString() {
		return src;
	}
	
	
	public ArrayList<StateRunnable> getMatches(StateRunnable baseSI) throws ContentError, ConnectionError, RuntimeError {
		return root.getMatches(baseSI);
	}

	
	public String getEvaluationProcessDescription() {
		String ret = "";
		ret += "'" + src + "' produces " + root.getEvaluationProcessDescription();
		
		return ret;
	}

	public void replaceSymbols(HashMap<String, String> map) {
	   root.replaceSymbols(map);
    }
	
	
}

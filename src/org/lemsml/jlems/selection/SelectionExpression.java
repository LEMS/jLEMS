package org.lemsml.jlems.selection;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.run.RuntimeError;
import org.lemsml.jlems.run.StateInstance;
import org.lemsml.jlems.run.StateRunnable;
import org.lemsml.jlems.sim.ContentError;

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

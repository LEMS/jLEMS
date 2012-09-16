package org.lemsml.selection;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.run.ConnectionError;
import org.lemsml.run.StateInstance;
import org.lemsml.util.ContentError;
import org.lemsml.util.RuntimeError;

public class SelectionExpression {

	String src;
	SelectionNode root;
	
	public SelectionExpression(String s, SelectionNode rn) {
		src = s;
		root = rn;
	}
	
	public String toString() {
		return getEvaluationProcessDescription();
	}
	
	
	public ArrayList<StateInstance> getMatches(StateInstance baseSI) throws ContentError, ConnectionError, RuntimeError {
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

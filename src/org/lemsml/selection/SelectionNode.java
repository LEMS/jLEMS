package org.lemsml.selection;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.expression.Node;
import org.lemsml.run.ConnectionError;
import org.lemsml.run.StateInstance;
import org.lemsml.util.ContentError;
import org.lemsml.util.RuntimeError;



public abstract class SelectionNode extends Node {

	public abstract String getEvaluationProcessDescription();

	public abstract ArrayList<StateInstance> getMatches(StateInstance baseSI) throws ContentError, ConnectionError, RuntimeError;

	public abstract void replaceSymbols(HashMap<String, String> map);
	 

}

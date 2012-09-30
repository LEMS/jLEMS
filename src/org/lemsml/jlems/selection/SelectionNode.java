package org.lemsml.jlems.selection;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.expression.Node;
import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.run.StateInstance;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.RuntimeError;



public abstract class SelectionNode extends Node {

	public abstract String getEvaluationProcessDescription();

	public abstract ArrayList<StateInstance> getMatches(StateInstance baseSI) throws ContentError, ConnectionError, RuntimeError;

	public abstract void replaceSymbols(HashMap<String, String> map);
	 

}

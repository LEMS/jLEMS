package org.lemsml.jlems.core.selection;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.core.expression.Node;
import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.run.StateInstance;
import org.lemsml.jlems.core.run.StateRunnable;
import org.lemsml.jlems.core.sim.ContentError;



public abstract class AbstractSelectionNode extends Node {

	public abstract String getEvaluationProcessDescription();

	public abstract ArrayList<StateRunnable> getMatches(StateRunnable baseSI) throws ContentError, ConnectionError, RuntimeError;

	public abstract void replaceSymbols(HashMap<String, String> map);
	 

}

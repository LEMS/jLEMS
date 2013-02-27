package org.lemsml.jlems.core.selection;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.run.StateInstance;
import org.lemsml.jlems.core.run.StateRunnable;
import org.lemsml.jlems.core.sim.ContentError;

public class SelectorNode extends AbstractSelectionNode {

	
	String sel;
	
	
	public SelectorNode(String stok) {
		super();
		sel = stok; 
	}

	
	public String toString() {
		return "sel(" + sel + ")";
	}

	public String getEvaluationProcessDescription() {
		String ret = "Select children matchhing '" + sel +"'";
		return ret;
	}

	
	@Override
	public ArrayList<StateRunnable> getMatches(StateRunnable baseSI) throws ContentError, ConnectionError, RuntimeError {
 		ArrayList<StateRunnable> asi = baseSI.getPathInstances(sel);
 		return asi;
	}


	public double getFloat(StateRunnable si) throws ContentError {
		return si.getFloatProperty(sel);
	}


	@Override
    public void replaceSymbols(HashMap<String, String> map) {
 		if (map.containsKey(sel)) {
			// E.info("replacing symbol " + sel + " with " + map.get(sel));
			sel = map.get(sel);
		}
    }
	
}

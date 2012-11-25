package org.lemsml.jlems.selection;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.run.ConnectionError;
import org.lemsml.jlems.run.RuntimeError;
import org.lemsml.jlems.run.StateInstance;
import org.lemsml.jlems.sim.ContentError;

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
	public ArrayList<StateInstance> getMatches(StateInstance baseSI) throws ContentError, ConnectionError, RuntimeError {
 		ArrayList<StateInstance> asi = baseSI.getPathInstances(sel);
 		return asi;
	}


	public double getFloat(StateInstance si) throws ContentError {
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

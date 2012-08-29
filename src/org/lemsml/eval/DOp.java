package org.lemsml.eval;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class DOp extends DVal {
	
	public DVal left;
	public DVal right;
	
	public DOp(DVal dvl, DVal dvr) {
		left = dvl;
		right = dvr;
	}
	 
	 
	public void recAdd(ArrayList<DVar> val) {
		left.recAdd(val);
		right.recAdd(val);
	}
	
	
	public void substituteVariableWith(String var, String sub) {
		left.substituteVariableWith(var, sub);
		right.substituteVariableWith(var, sub);
	}
	
	
	public boolean variablesIn(HashSet<String> known) {
		boolean ret = false;
		if (left.variablesIn(known) && right.variablesIn(known)) {
			ret = true;
		}
		return ret;
	}
}

package org.lemsml.jlems.core.eval;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class AbstractDOp extends AbstractDVal {
	
	public AbstractDVal left;
	public AbstractDVal right;
	
	public AbstractDOp(AbstractDVal dvl, AbstractDVal dvr) {
		super();
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
	
	public boolean isTrivial() {
		return false;
	}
}

package org.lemsml.run;
 
import java.util.HashMap;

import org.lemsml.eval.BBase;
import org.lemsml.util.RuntimeError;

public class ConditionAction {
 
	
	BBase condition;
	
	ActionBlock action;
	
	
	public ConditionAction(BBase bb) {
		 condition = bb;
	}



	public void setAction(ActionBlock ea) {
		action = ea; 
	}



	public ActionBlock getAction() {
		return action;
	}



	public Boolean eval(HashMap<String, Double> varHM) {
		return condition.eval(varHM);
	}



	public Boolean evalptr(HashMap<String, DoublePointer> varHM) throws RuntimeError {
		return condition.evalptr(varHM);
	}

	
}

package org.lemsml.jlems.run;
 
import java.util.HashMap;

import org.lemsml.jlems.eval.BBase;

public class ConditionAction {
 
	
	BBase condition;
	
	ActionBlock action;
	
	
	public ConditionAction(BBase bb) {
		 condition = bb;
	}


	public ConditionAction makeCopy() {
		ConditionAction ret= new ConditionAction(condition.makeCopy());
		ret.setAction(action.makeCopy());
		return ret;
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

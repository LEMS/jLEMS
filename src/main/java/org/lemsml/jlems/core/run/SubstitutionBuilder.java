package org.lemsml.jlems.core.run;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
 
public class SubstitutionBuilder extends BuilderElement {

	 
	RuntimeType runtimeType;
	LocalValues lpvals;
 	
	public SubstitutionBuilder(RuntimeType rt, LocalValues lv) {
		super(); 
		this.runtimeType = rt;
		this.lpvals = lv;
	}
	

	
	public StateInstance buildSubstitute(StateType origType) throws ContentError, ConnectionError, RuntimeError {
		
		StateInstance ret = null;
		
		if (runtimeType instanceof StateType) {
			StateType stateType = (StateType)runtimeType;
			StateInstance sr = stateType.newInstance();
			// sr.setParent(par);
			
			sr.setLocalValues(lpvals);
			ret = sr;
		
		} else {
			E.missing("Time to build a substitute from a non state type: " + runtimeType);
		}
		
		return ret;
	}
	
	
	
	
	public boolean isInstantiator() {
		return true;
	}

	
	

	public void addAssignment(String property, String expression) {
		// MUSTDO
		
	}

	//@Override
	public void consolidateStateTypes() throws ContentError {
		 if (runtimeType instanceof StateType) {
			 runtimeType = ((StateType)runtimeType).getConsolidatedStateType("(child)");
		 }	
	}
 

}

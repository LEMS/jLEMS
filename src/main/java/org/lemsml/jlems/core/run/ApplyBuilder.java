package org.lemsml.jlems.core.run;

import java.util.ArrayList;
import java.util.HashMap;
 
import org.lemsml.jlems.core.sim.ContentError;
 


public class ApplyBuilder extends AbstractPostBuilder {
 
	String listName;
 	
	public ApplyBuilder(String mnm, ArrayList <StateType> a) {
		super();
		listName = mnm;
 	}

	public String toString() {
		return "ApplyBuilder(" + listName + ") nchildren=" + elts.size();
	}
	
	
	@Override
	public void postBuild(StateRunnable tgt, HashMap<String, StateRunnable> sihm, BuildContext bc) throws ConnectionError,
			ContentError, RuntimeError {
 	 
		MultiInstance mi = ((StateInstance)tgt).getMultiInstance(listName);
 
		for (StateRunnable si : mi.getInstances()) {		
			si.checkBuilt();
			postChildren(si, null, bc);
		}
	}

	@Override
	public void consolidateStateTypes() {
		// nothing to do here - we don't create any children directly
	}
 
 

}

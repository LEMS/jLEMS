package org.lemsml.jlems.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.sim.ContentError;
 


public class ApplyBuilder extends AbstractPostBuilder {
 
	String listName;
 	
	public ApplyBuilder(String mnm, ArrayList <ComponentBehavior> a) {
		super();
		listName = mnm;
 	}

	public String toString() {
		return "ApplyBuilder(" + listName + ") nchildren=" + elts.size();
	}
	
	
	@Override
	public void postBuild(StateInstance tgt, HashMap<String, StateInstance> sihm, BuildContext bc) throws ConnectionError,
			ContentError, RuntimeError {
 	 
		MultiInstance mi = tgt.getMultiInstance(listName);
 
		for (StateInstance si : mi.getInstances()) {		
			si.checkBuilt();
			postChildren(si, null, bc);
		}
	}

	@Override
	public void consolidateComponentBehaviors() {
		E.missing();
	}
 
 

}

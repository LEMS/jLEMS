package org.lemsml.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.util.ContentError;
import org.lemsml.util.RuntimeError;

public class ApplyBuilder extends PostBuilder {
 
	String listName;
 	
	public ApplyBuilder(String mnm, ArrayList <ComponentBehavior> a) {
		listName = mnm;
 	}

	public String toString() {
		return "ApplyBuilder(" + listName + ") nchildren=" + elts.size();
	}
	
	
	@Override
	public void postBuild(StateInstance tgt, HashMap<String, StateInstance> sihm, BuildContext bc) throws ConnectionError,
			ContentError, RuntimeError {
 	 
		MultiInstance mi = tgt.getMultiInstance(listName);
 
		for (StateInstance si : mi.instances) {		
			si.checkBuilt();
			postChildren(si, null, bc);
		}
	}

	@Override
	public void consolidateComponentBehaviors() {
	 
	}
 
 

}

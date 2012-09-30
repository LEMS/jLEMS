package org.lemsml.jlems.run;

import java.util.HashMap;

import org.lemsml.jlems.sim.RunnableAccessor;
import org.lemsml.jlems.util.E;

public class WhenBuilder extends BuilderElement {

	String test;
	
	
	public WhenBuilder(String t) {
		 test = t;
	}


	 
	public void postBuild(RunnableAccessor ra, StateRunnable base, HashMap<String, StateInstance> sihm) {
		E.missing("When not implemented");
	}



	@Override
	public void consolidateComponentBehaviors() {
		// TODO Auto-generated method stub
		
	}

}

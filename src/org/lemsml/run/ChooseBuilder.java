package org.lemsml.run;

import java.util.HashMap;

import org.lemsml.sim.RunnableAccessor;
import org.lemsml.util.E;

public class ChooseBuilder extends BuilderElement {

 
	public void postBuild(RunnableAccessor ra, StateRunnable base, HashMap<String, StateInstance> sihm) {
		E.missing("choose not implemented");
		
	}

	@Override
	public void consolidateComponentBehaviors() {
		// TODO Auto-generated method stub
		
	}

}

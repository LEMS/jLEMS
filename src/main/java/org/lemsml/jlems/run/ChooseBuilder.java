package org.lemsml.jlems.run;

import java.util.HashMap;

import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.sim.RunnableAccessor;

public class ChooseBuilder extends BuilderElement {

 
	public void postBuild(RunnableAccessor ra, StateRunnable base, HashMap<String, StateInstance> sihm) {
		E.missing("choose not implemented");
		
	}

	@Override
	public void consolidateStateTypes() {
		// TODO Auto-generated method stub
		
	}

}

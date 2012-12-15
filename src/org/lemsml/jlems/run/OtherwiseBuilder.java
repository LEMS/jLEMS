package org.lemsml.jlems.run;

import java.util.HashMap;

import org.lemsml.jlems.sim.ContentError;


public class OtherwiseBuilder extends AbstractPostBuilder {

 
	public void postBuild(StateRunnable base, HashMap<String, StateRunnable> sihm, BuildContext bc) throws ConnectionError, ContentError, RuntimeError {
		super.postChildren(base, sihm, bc);	
	}

	@Override
	public void consolidateStateTypes() {
		// TODO Auto-generated method stub
		
	}
 
}

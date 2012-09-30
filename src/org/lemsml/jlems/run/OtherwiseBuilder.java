package org.lemsml.jlems.run;

import java.util.HashMap;

import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.RuntimeError;

public class OtherwiseBuilder extends PostBuilder {

 
	public void postBuild(StateInstance base, HashMap<String, StateInstance> sihm, BuildContext bc) throws ConnectionError, ContentError, RuntimeError {
		super.postChildren(base, sihm, bc);	
	}

	@Override
	public void consolidateComponentBehaviors() {
		// TODO Auto-generated method stub
		
	}
 
}

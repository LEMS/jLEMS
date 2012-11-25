package org.lemsml.jlems.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.sim.ContentError;


public class ForEachBuilder extends AbstractPostBuilder {

	String path;
	String var;
	
	
	public ForEachBuilder(String instances, String as) {
		super();
		path = instances;
		var = as;
	}


	 
	public void postBuild(StateInstance base, HashMap<String, StateInstance> sihm, BuildContext bc) throws ConnectionError, ContentError, RuntimeError {


		//E.info("postBuild on: " + base + ", bc: " + bc);
		HashMap<String, StateInstance> passHM = null;

		if (sihm == null) {
			passHM = new HashMap<String, StateInstance>();
		} else {
			passHM = sihm;
		}
		// MUSTDO base is not the right starting point: should be relative to the link target in enclosing cpt
		ArrayList<StateInstance> asi = base.getStateInstances(path);
 		
		for (StateInstance si : asi) {
			passHM.put(var, si);		
	 		postChildren(base, passHM, bc);	
		}
	}



	@Override
	public void consolidateComponentBehaviors() {
		// TODO Auto-generated method stub
		
	}	
	

 

}

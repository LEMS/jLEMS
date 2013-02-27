package org.lemsml.jlems.core.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.core.sim.ContentError;


public class ForEachBuilder extends AbstractPostBuilder {

	String path;
	String var;
	
	
	public ForEachBuilder(String instances, String as) {
		super();
		path = instances;
		var = as;
	}


	 
	public void postBuild(StateRunnable base, HashMap<String, StateRunnable> sihm, BuildContext bc) throws ConnectionError, ContentError, RuntimeError {


		//E.info("postBuild on: " + base + ", bc: " + bc);
		HashMap<String, StateRunnable> passHM = null;

		if (sihm == null) {
			passHM = new HashMap<String, StateRunnable>();
		} else {
			passHM = sihm;
		}
		// MUSTDO base is not the right starting point: should be relative to the link target in enclosing cpt
		ArrayList<StateRunnable> asi = base.getStateInstances(path);
 		
		for (StateRunnable si : asi) {
			passHM.put(var, si);		
	 		postChildren(base, passHM, bc);	
		}
	}



	@Override
	public void consolidateStateTypes() {
		// TODO Auto-generated method stub
		
	}	
	

 

}

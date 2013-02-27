package org.lemsml.jlems.core.run;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
 

public class CoBuilder extends AbstractChildBuilder {

	Component ctr;
	
	StateType stateType;
 
	
	
	public CoBuilder(Component c, StateType cb) {
		super(); 
		ctr = c;
		 stateType = cb;
	}
	
	
	public void childInstantiate(StateInstance par) throws ContentError, ConnectionError, RuntimeError {
  		
		// MultiInstance mi = new MultiInstance(stateType.typeName, "");
		 
		StateRunnable si = par.getScopeInstance(ctr.getID());
		InstanceSet<StateRunnable> iset = si.getUniqueInstanceSet();
		
		iset.getParent().checkBuilt();
		
		for (StateRunnable psi : iset.getItems()) {
			StateInstance sr = stateType.newInstance();
			sr.coCopy((StateInstance)psi);
			par.addListChild(stateType.getTypeName(), sr.getID(), sr);
		//	mi.add(sr);
		}
		// par.addMultiInstance(mi);
	}
	
	
	
	
	public boolean isInstantiator() {
		return true;
	}


	@Override
	public void consolidateStateTypes() {
		 if (stateType != null) {
			 stateType = stateType.getConsolidatedStateType("(cobuilder)");
		 }	
	}

 

 
}

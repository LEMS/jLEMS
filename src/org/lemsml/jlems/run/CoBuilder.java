package org.lemsml.jlems.run;

import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.type.Component;
 

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
		 
		StateInstance si = par.getScopeInstance(ctr.getID());
		InstanceSet<StateInstance> iset = si.getUniqueInstanceSet();
		
		iset.getParent().checkBuilt();
		
		for (StateInstance psi : iset.getItems()) {
			StateInstance sr = stateType.newInstance();
			sr.coCopy(psi);
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

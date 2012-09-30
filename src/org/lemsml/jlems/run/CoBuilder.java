package org.lemsml.jlems.run;

import org.lemsml.jlems.type.Component;
import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.RuntimeError;

public class CoBuilder extends AbstractChildBuilder {

	Component ctr;
	
	ComponentBehavior componentBehavior;
 
	
	
	public CoBuilder(Component c, ComponentBehavior cb) {
		 ctr = c;
		 componentBehavior = cb;
	}
	
	
	public void childInstantiate(StateInstance par) throws ContentError, ConnectionError, RuntimeError {
  		
		MultiInstance mi = new MultiInstance(componentBehavior.typeName, "");
		 
		StateInstance si = par.getScopeInstance(ctr.getID());
		
		InstanceSet<StateInstance> iset = si.getUniqueInstanceSet();
		
		iset.getParent().checkBuilt();
		
		for (StateInstance psi : iset.getItems()) {
			StateInstance sr = componentBehavior.newInstance();
			sr.coCopy(psi);
			mi.add(sr);
		}
		par.addMultiInstance(mi);
	}
	
	public boolean isInstantiator() {
		return true;
	}


	@Override
	public void consolidateComponentBehaviors() {
		 if (componentBehavior != null) {
			 componentBehavior = componentBehavior.getConsolidatedComponentBehavior("(cobuilder)");
		 }	
	}

 

 
}

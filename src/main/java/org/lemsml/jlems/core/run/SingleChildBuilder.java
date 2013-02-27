package org.lemsml.jlems.core.run;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;


 

public class SingleChildBuilder extends BuilderElement implements ChildInstantiator {

	 
	RuntimeType runtimeType;
	
	String name;
	
	public SingleChildBuilder(String snm, RuntimeType cb) {
		super(); 
		runtimeType = cb;
		 name = snm;
	}
	
	
	public void childInstantiate(StateInstance parent) throws ContentError, ConnectionError, RuntimeError {
		if (runtimeType instanceof StateType) {
			StateType stateType = (StateType)runtimeType;
			StateInstance sr = stateType.newInstance();
			sr.setParent(parent);
			parent.addChild(name, sr);
		
		} else {
			E.info("Time to build from a non state type: " + runtimeType);
			StateRunnable sr = runtimeType.newStateRunnable();
			sr.setParent(parent);
			parent.addChild(name, sr);
		}
	}
 

	public void addAssignment(String property, String expression) {
		// MUSTDO
		
	}

	@Override
	public void consolidateStateTypes() {
		 if (runtimeType instanceof StateType) {
			 runtimeType = ((StateType)runtimeType).getConsolidatedStateType("(child)");
		 }	
	}
 

}

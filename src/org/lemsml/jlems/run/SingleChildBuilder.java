package org.lemsml.jlems.run;

import org.lemsml.jlems.sim.ContentError;


 

public class SingleChildBuilder extends BuilderElement implements ChildInstantiator {

	 
	StateType stateType;
	
	String name;
	
	public SingleChildBuilder(String snm, StateType cb) {
		super(); 
		stateType = cb;
		 name = snm;
	}
	
	
	public void childInstantiate(StateInstance parent) throws ContentError, ConnectionError, RuntimeError {
		StateInstance sr = stateType.newInstance();
 		sr.setParent(parent);
		parent.addChild(name, (StateInstance)sr);
	}
 

	public void addAssignment(String property, String expression) {
		// MUSTDO
		
	}

	@Override
	public void consolidateStateTypes() {
		 if (stateType != null) {
			 stateType = stateType.getConsolidatedStateType("(child)");
		 }	
	}
 

}

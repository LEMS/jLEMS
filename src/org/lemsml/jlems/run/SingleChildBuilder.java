package org.lemsml.jlems.run;

import org.lemsml.jlems.util.ContentError;
import org.lemsml.jlems.util.RuntimeError;
 

public class SingleChildBuilder extends BuilderElement implements ChildInstantiator {

	 
	ComponentBehavior componentBehavior;
	
	String name;
	
	public SingleChildBuilder(String snm, ComponentBehavior cb) {
		 componentBehavior = cb;
		 name = snm;
	}
	
	
	public void childInstantiate(StateInstance parent) throws ContentError, ConnectionError, RuntimeError {
 
		StateInstance sr = componentBehavior.newInstance();
		sr.setParent(parent);
		parent.addChild(name, (StateInstance)sr);
	}
 

	public void addAssignment(String property, String expression) {
		// MUSTDO
		
	}

	@Override
	public void consolidateComponentBehaviors() {
		 if (componentBehavior != null) {
			 componentBehavior = componentBehavior.getConsolidatedComponentBehavior("(child)");
		 }	
	}
 

}

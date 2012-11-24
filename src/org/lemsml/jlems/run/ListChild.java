package org.lemsml.jlems.run;

public class ListChild {

	
	String listName;
	ComponentBehavior componentBehavior;
	
	
	public ListChild(String lnm, ComponentBehavior cb) {
		listName = lnm;
		componentBehavior = cb;
	}


	public String getListName() {
		return listName;
	}
	
	public ComponentBehavior getComponentBehavior() {
		return componentBehavior;
	}


	public String getTypeName() {
		return componentBehavior.getTypeName();
	}
}

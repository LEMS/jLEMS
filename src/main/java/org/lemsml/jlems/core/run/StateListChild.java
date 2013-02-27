package org.lemsml.jlems.core.run;

public class StateListChild {

	
	String listName;
	StateInstance instance;
	
	public StateListChild(String s, StateInstance inst) {
		listName = s;
		instance = inst;
	}

	public StateInstance getInstance() {
		return instance;
	}

}

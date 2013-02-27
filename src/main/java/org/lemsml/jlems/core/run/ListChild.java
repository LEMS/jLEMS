package org.lemsml.jlems.core.run;

public class ListChild {

	
	String listName;
	StateType stateType;
	
	
	public ListChild(String lnm, StateType cb) {
		listName = lnm;
		stateType = cb;
	}


	public String getListName() {
		return listName;
	}
	
	public StateType getStateType() {
		return stateType;
	}


	public String getTypeName() {
		return stateType.getTypeName();
	}
}

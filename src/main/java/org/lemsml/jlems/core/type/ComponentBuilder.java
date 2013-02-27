package org.lemsml.jlems.core.type;


public class ComponentBuilder {

	Component target;
	
	
	public ComponentBuilder() {
		target = new Component();
 	}


	public void setID(String s) {
		target.setID(s);
	}

 


	public Component getTarget() {
		return target;
	}


	public void addParameter(String nm, String val) {
		target.setParameter(nm, val);
	}


	public void setType(String typeName) {
		target.setType(typeName);
	}

}

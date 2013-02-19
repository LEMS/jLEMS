package org.lemsml.jlems.type;

public class Insertion {

	
	public String component;

	public Insertion() {
		// TODO - only one
	}
	
	
	public Insertion(String cpt) {
		component = cpt;
	}
	
	
	public Insertion makeCopy() {
		Insertion ret = new Insertion(component);
		return ret;
	}
	
	
}


package org.lemsml.jlems.codger;

public class Instantiation {

	String name;
	String type;
	
	
	
	public Instantiation(String s, String t) {
		name = s;
		type = t;
	}

	public String generateJava() {
		String ret = "" + name + " = new " + type + "();";
		return ret;
	}

}

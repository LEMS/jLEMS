package org.lemsml.jlems.codger;

public abstract class MetaField {

	public enum Type {DOUBLE, INTEGER, STRING, VOID}
	String name;
	
	
	public MetaField(String nm) {
		name = nm;
	}
	
	public abstract String generateJava();
	
}


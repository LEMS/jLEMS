package org.lemsml.canonical;

public class CanonicalAttribute {

	String name;
	String value;
	
	public CanonicalAttribute(String sn, String sv) {
		name = sn;
		value = sv;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
}

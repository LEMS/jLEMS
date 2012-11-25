package org.lemsml.jlems.codger;

public class FixedMetaField extends AbstractMetaField {

	
	double value;
	
	public FixedMetaField(String nm, double val) {
		super(nm);
		value = val;
	}
	
	
	public String generateJava() {
		return "private final static double " + name + " = " + value + ";";
	}
	
	
}

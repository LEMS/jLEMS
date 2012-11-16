package org.lemsml.jlems.codger;

public class FixedMetaField extends MetaField {

	
	double value;
	
	public FixedMetaField(String nm, double val) {
		super(nm);
		value = val;
	}
	
	
	public String generateJava() {
		return "    private final static double " + name + " = " + value + ";\n";
	}
	
	
}

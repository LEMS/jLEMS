package org.lemsml.jlems.codger.metaclass;

public class FixedField extends AbstractField {

	
	double value;
	
	public FixedField(String nm, double val) {
		super(nm);
		value = val;
	}
	
	
	public String generateJava() {
		return "private final static double " + name + " = " + value + ";";
	}
	
	
}

package org.lemsml.jlems.codger;

public class VariableMetaField extends MetaField {

	
	public VariableMetaField(String nm) {
		super(nm);
	}
 
	public String generateJava() {
		return "    private double " + name + " = 0.0;\n";
	}
	
}

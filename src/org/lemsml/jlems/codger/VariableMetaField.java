package org.lemsml.jlems.codger;

public class VariableMetaField extends AbstractMetaField {

	
	public VariableMetaField(String nm) {
		super(nm);
	}
 
	public String generateJava() {
		return "    private double " + name + " = 0.0;\n";
	}
	
}

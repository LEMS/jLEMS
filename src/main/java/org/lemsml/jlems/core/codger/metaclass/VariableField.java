package org.lemsml.jlems.core.codger.metaclass;


public class VariableField extends AbstractField {

	
	public VariableField(String nm) {
		super(nm);
	}
 
	public String generateJava() {
		return "    private double " + name + " = 0.0;\n";
	}
	
}

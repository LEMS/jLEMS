package org.lemsml.jlems.core.codger.metaclass;

public class FloatMethodArgument extends AbstractMethodArgument {

	public FloatMethodArgument(String s) {
		super(s);
	}

 
	public String generateJava() {
		return "double " + name;
	}
	
	public String generateCallJava() {
		return name;
	}
	
}

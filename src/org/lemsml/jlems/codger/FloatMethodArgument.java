package org.lemsml.jlems.codger;

public class FloatMethodArgument extends MethodArgument {

	public FloatMethodArgument(String s) {
		super(s);
	}

 
	public String generateJava() {
		return "double " + name;
	}
	
}

package org.lemsml.jlems.core.codger.metaclass;

public class StringMethodArgument extends AbstractMethodArgument {

	public StringMethodArgument(String s) {
		super(s);
	}

 
	public String generateJava() {
		return "String " + name;
	}
	
	public String generateCallJava() {
		return name;
	}
	
}

package org.lemsml.jlems.codger;

public class MethodCall extends Operation {

	String methodName;
	
	public MethodCall(String str) {
		methodName = str;
	}

	@Override
	public String generateJava() {
		return  "" + methodName + "();";
	}
 
}

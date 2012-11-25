package org.lemsml.jlems.codger;

public class MethodCall extends AbstractOperation {

	String methodName;
	
	public MethodCall(String str) {
		super();
		methodName = str;
	}

	@Override
	public String generateJava() {
		return  "" + methodName + "();";
	}
 
}

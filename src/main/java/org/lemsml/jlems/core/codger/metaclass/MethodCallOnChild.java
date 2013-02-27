package org.lemsml.jlems.core.codger.metaclass;

public class MethodCallOnChild extends AbstractOperation {

	String objName;
	Method method;
	
	
	public MethodCallOnChild(String onm, Method mm) {
		objName = onm;
		method = mm;
	}


	@Override
	public String generateJava() {
		String ret = "" + objName + "." + method.generateCallJava();
		return ret;
	}
	
	
}

package org.lemsml.jlems.codger.metaclass;

public class LocalMethodCall extends AbstractOperation {
 
	Method method;
	
	
	public LocalMethodCall(Method mm) {
		method = mm;
	}


	@Override
	public String generateJava() {
		String ret = "" + method.generateCallJava();
		return ret;
	}
	
	
}

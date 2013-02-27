package org.lemsml.jlems.core.codger.metaclass;

public class MethodCallOnChildren extends AbstractOperation {

	String typName;
	String arrName;
	Method method;
	
	
	public MethodCallOnChildren(String anm, String atnm, Method mm) {
		typName = atnm;
		arrName = anm;
		method = mm;
	}


	@Override
	public String generateJava() {
		String ret = "for (" + typName + " elt : " + arrName + ") {\n";
		ret += "    elt." + method.generateCallJava() + "\n";
		ret += "}\n";
 		return ret;
	}
	
	
}

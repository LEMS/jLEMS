package org.lemsml.jlems.core.codger.metaclass;

public class FloatMethodAssignment extends AbstractOperation {

	String varName;
	String methodName;
	
	public FloatMethodAssignment(String vnm, String mnm) {
		super();
		varName = vnm;
		methodName = mnm;
	}

	@Override
	public String generateJava() {
		String ret = "" + varName + " = " + methodName + "();";
		return ret;
	}

 
}

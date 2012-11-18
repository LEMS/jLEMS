package org.lemsml.jlems.codger;

public class FloatAssignment extends Operation {

	String varName;
	String expr;
	
	public FloatAssignment(String vnm, String ex) {
		varName = vnm;
		expr = ex;
	}

	@Override
	public String generateJava() {
		String ret = "" + varName + " = " + expr + ";";
		return ret;
	}

}

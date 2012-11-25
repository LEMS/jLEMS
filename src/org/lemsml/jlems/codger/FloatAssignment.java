package org.lemsml.jlems.codger;

public class FloatAssignment extends AbstractOperation {

	String varName;
	String expr;
	
	public FloatAssignment(String vnm, String ex) {
		super();
		varName = vnm;
		expr = ex;
	}

	@Override
	public String generateJava() {
		String ret = "" + varName + " = " + expr + ";";
		return ret;
	}

}

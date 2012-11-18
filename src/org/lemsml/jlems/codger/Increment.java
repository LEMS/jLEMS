package org.lemsml.jlems.codger;

public class Increment extends Operation {

	String variable;
	Expression value;
	
	public Increment(String var, Expression val) {
		variable = var;
		value = val;
	}

	@Override
	public String generateJava() {
		String ret = "" + variable + " += " + value.generateJava() + ";";
		return ret;
	}
	
	
}

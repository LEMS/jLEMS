package org.lemsml.jlems.core.codger.metaclass;


public class Product extends AbstractExpression {

	String a;
	String b;
	
	
	public Product(String sa, String sb) {
		super();
		a = sa;
		b = sb;
	}
	
	public String generateJava() {
		String ret = "" + a + " * " + b;
		// return "(" + a + ") * (" + b + ")";
		return ret;
	}
	
	
}

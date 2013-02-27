package org.lemsml.jlems.core.run;

public class FixedQuantity {

	String name;
	double value;
	
	public FixedQuantity(String s, double d) {
		name = s;
		value = d;	
	}
	
	public String getName() {
		return name;
	}
	
	public double getValue() {
		return value;
	}
	
}

package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.expression.Valued;

public class IndVar implements Valued {

	double val = Double.NaN;
	
	String name;
	
	
	public IndVar(String s) {
		name = s;
	}
	
	
	public double getValue() {
		return val;
	}

 
	public boolean isFixed() {
		return false;
	}

	 
	public String getName() {
		return name;
	}

}

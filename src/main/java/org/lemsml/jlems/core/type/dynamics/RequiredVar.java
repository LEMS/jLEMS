package org.lemsml.jlems.core.type.dynamics;

import org.lemsml.jlems.core.expression.Dimensional;
import org.lemsml.jlems.core.expression.Valued;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.type.Dimension;

public class RequiredVar implements Valued {

	double val = Double.NaN;
	
	String name;
	public Dimension r_dimension;
	
	public RequiredVar(String s, Dimension d) {
		name = s;
		r_dimension = d;
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



	public Dimensional getDimensionality() {
		if (r_dimension == null) {
			E.error("null dimension in required var " + name);
		}
		return r_dimension;
	}

}

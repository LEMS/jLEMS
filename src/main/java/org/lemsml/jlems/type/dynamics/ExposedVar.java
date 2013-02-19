package org.lemsml.jlems.type.dynamics;

import org.lemsml.jlems.expression.Dimensional;
import org.lemsml.jlems.expression.Valued;
import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.type.Dimension;

public class ExposedVar implements Valued {

	double val = Double.NaN;
	
	String name;
	public Dimension r_dimension;
	
	public ExposedVar(String s, Dimension d) {
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

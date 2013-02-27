package org.lemsml.jlems.core.run;

import java.util.HashMap;

public interface GeneratedInstance {

	public void advance(double t, double dt, HashMap<String, DoublePointer> vars);

	public double getVariable(String varname);

	// public void advance(double dt);
	
}

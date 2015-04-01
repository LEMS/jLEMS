package org.lemsml.jlems.core.run;

import java.util.HashMap;

public interface GeneratedInstance {

	void advance(double t, double dt, HashMap<String, DoublePointer> vars);

	double getVariable(String varname);

	// public void advance(double dt);
	
}

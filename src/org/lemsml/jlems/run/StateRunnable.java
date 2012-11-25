package org.lemsml.jlems.run;

import java.util.HashMap;

import org.lemsml.jlems.display.LineDisplay;
import org.lemsml.jlems.sim.ContentError;

public interface StateRunnable {

	
	StateRunnable getChild(String snm) throws ConnectionError;

	void advance(StateRunnable parent, double t, double dt) throws RuntimeError, ContentError;

	void exportState(String pfx, double t, LineDisplay ld);

	InPort getFirstInPort() throws ConnectionError;

	InPort getInPort(String portId) throws ConnectionError;

	StateWrapper getWrapper(String string);

	HashMap<String, DoublePointer> getVariables();
	
	String stateString();

	void setVariable(String varname, double d);
	
	double getVariable(String varname) throws RuntimeError;
	
	String getID();
 

	void setNewVariable(String string, double d);

	void evaluate(StateRunnable parent) throws RuntimeError, ContentError;

 
}

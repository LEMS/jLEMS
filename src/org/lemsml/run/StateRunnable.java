package org.lemsml.run;

import java.util.HashMap;

import org.lemsml.display.LineDisplay;
import org.lemsml.util.ContentError;
import org.lemsml.util.RuntimeError;

public interface StateRunnable {

	
	public StateRunnable getChild(String snm) throws ConnectionError;

	public void advance(StateRunnable parent, double t, double dt) throws RuntimeError, ContentError;

	public void exportState(String pfx, double t, LineDisplay ld);

	public InPort getFirstInPort() throws ConnectionError;

	public InPort getInPort(String portId) throws ConnectionError;

	public StateWrapper getWrapper(String string);

	public HashMap<String, DoublePointer> getVariables();
	
	public String stateString();

	public void setVariable(String varname, double d);
	
	public double getVariable(String varname) throws RuntimeError;
	
	public String getID();
 

	public void setNewVariable(String string, double d);

	public void evaluate(StateRunnable parent) throws RuntimeError, ContentError;

 
}

package org.lemsml.jlems.core.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.core.display.LineDisplay;
import org.lemsml.jlems.core.sim.ContentError;

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

	void initialize(StateRunnable sr) throws RuntimeError, ContentError;

	Object getComponentID();

	boolean hasChildInstance(String str) throws ContentError;
	
	StateRunnable getChildInstance(String string) throws ContentError;

	ArrayList<StateRunnable> quietGetStateInstances(String path) throws ConnectionError, ContentError, RuntimeError;

	void setParent(StateRunnable par);

	ArrayList<StateRunnable> getStateInstances() throws ConnectionError, ContentError, RuntimeError;

	void checkBuilt() throws ConnectionError, ContentError, RuntimeError;

	StateRunnable getScopeInstance(String id);

	ArrayList<StateRunnable> getPathInstances(String sel) throws ContentError, ConnectionError, RuntimeError;

	double quietGetFloatProperty(String sel) throws ContentError;

	boolean hasSingleMI();

	OutPort getOutPort(String sourcePortId);

	StateRunnable getPathStateInstance(String path) throws ContentError;

	OutPort getFirstOutPort() throws ConnectionError;

	StateRunnable getParent();

	InstanceSet<StateRunnable> getUniqueInstanceSet() throws ContentError;

	InstanceSet<StateRunnable> getInstanceSet(String col);

	Object getWork();

	ArrayList<StateRunnable> getStateInstances(String path) throws ConnectionError, ContentError, RuntimeError;

	double getFloatProperty(String sel) throws ContentError;

	String getPathStringValue(String fieldName, double fac, double off) throws ContentError, RuntimeError;

	void addAttachment(String destAttachments, StateInstance rsi) throws ConnectionError, ContentError, RuntimeError;

	void addAttachment(StateInstance rsi) throws ConnectionError, ContentError, RuntimeError;

	MultiInstance getSingleMI();

	void setList(String childrenName);

	String getChildSummary();

	boolean isBuilt();

	String getDimensionString(String lastbit) throws ContentError;

 
}

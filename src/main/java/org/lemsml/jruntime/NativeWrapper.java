package org.lemsml.jruntime;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.core.display.LineDisplay;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.ConnectionError;
import org.lemsml.jlems.core.run.DoublePointer;
import org.lemsml.jlems.core.run.GeneratedInstance;
import org.lemsml.jlems.core.run.InPort;
import org.lemsml.jlems.core.run.InstanceSet;
import org.lemsml.jlems.core.run.MultiInstance;
import org.lemsml.jlems.core.run.OutPort;
import org.lemsml.jlems.core.run.RuntimeError;
import org.lemsml.jlems.core.run.StateInstance;
import org.lemsml.jlems.core.run.StateRunnable;
import org.lemsml.jlems.core.run.StateWrapper;
import org.lemsml.jlems.core.sim.ContentError;

public class NativeWrapper implements StateRunnable {

	GeneratedInstance target;
	
	NativeType nativeType;
	
	public NativeWrapper(GeneratedInstance geninst, NativeType nt) {
		target = geninst;
		nativeType = nt;
	}

	
	

	@Override
	public void advance(StateRunnable parent, double t, double dt) throws RuntimeError, ContentError {
		
		long wkTime = 0;
		if (nativeType.trackTime) {
			wkTime = System.nanoTime();
		}
		
		HashMap<String, DoublePointer> vars = parent.getVariables();
		target.advance(t, dt, vars);
	
		if (nativeType.trackTime) {
			nativeType.addTime(System.nanoTime() - wkTime);
		}
		
	}
	
	
	@Override
	public double getVariable(String varname) throws RuntimeError {
		return target.getVariable(varname);
	}

	

	@Override
	public StateRunnable getChild(String snm) throws ConnectionError {
		E.missing("Native wrapper");
		return null;
	}




	@Override
	public void exportState(String pfx, double t, LineDisplay ld) {
		E.missing("Native wrapper");
		
	}


	@Override
	public InPort getFirstInPort() throws ConnectionError {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public InPort getInPort(String portId) throws ConnectionError {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public StateWrapper getWrapper(String string) {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public HashMap<String, DoublePointer> getVariables() {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public String stateString() {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public void setVariable(String varname, double d) {
		E.missing("Native wrapper");
		
	}


	@Override
	public String getID() {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public void setNewVariable(String string, double d) {
		E.missing("Native wrapper");
		
	}


	@Override
	public void evaluate(StateRunnable parent) throws RuntimeError, ContentError {
		E.missing("Native wrapper");
		
	}


	@Override
	public void initialize(StateRunnable sr) throws RuntimeError, ContentError {
		//E.missing("Native wrapper");
		// TODO - nothing to do here?
	}


	@Override
	public Object getComponentID() {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public StateRunnable getChildInstance(String string) throws ContentError {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public ArrayList<StateRunnable> quietGetStateInstances(String path) throws ConnectionError, ContentError,
			RuntimeError {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public void setParent(StateRunnable par) {
		// shouldn't need this?
		
	}


	@Override
	public ArrayList<StateRunnable> getStateInstances() throws ConnectionError, ContentError, RuntimeError {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public void checkBuilt() throws ConnectionError, ContentError, RuntimeError {
	// not needed
		
	}


	@Override
	public StateRunnable getScopeInstance(String id) {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public ArrayList<StateRunnable> getPathInstances(String sel) throws ContentError, ConnectionError, RuntimeError {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public double quietGetFloatProperty(String sel) throws ContentError {
		E.missing("Native wrapper");
		return 0;
	}


	@Override
	public boolean hasSingleMI() {
		E.missing("Native wrapper");
		return false;
	}


	@Override
	public OutPort getOutPort(String sourcePortId) {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public StateRunnable getPathStateInstance(String path) throws ContentError {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public OutPort getFirstOutPort() {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public StateRunnable getParent() {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public InstanceSet<StateRunnable> getUniqueInstanceSet() throws ContentError {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public InstanceSet<StateRunnable> getInstanceSet(String col) {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public Object getWork() {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public ArrayList<StateRunnable> getStateInstances(String path) throws ConnectionError, ContentError, RuntimeError {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public double getFloatProperty(String sel) throws ContentError {
		E.missing("Native wrapper");
		return 0;
	}


	@Override
	public String getPathStringValue(String fieldName, double fac, double off) throws ContentError, RuntimeError {
		E.missing("Native wrapper");
		return null;
	}


	@Override
	public void addAttachment(String destAttachments, StateInstance rsi) throws ConnectionError, ContentError,
			RuntimeError {
		E.missing("Native wrapper");
		
	}


	@Override
	public MultiInstance getSingleMI() {
		E.missing("Native wrapper");
		return null;
	}

}

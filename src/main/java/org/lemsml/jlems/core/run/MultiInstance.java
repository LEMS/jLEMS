package org.lemsml.jlems.core.run;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.jlems.core.display.LineDisplay;
import org.lemsml.jlems.core.sim.ContentError;

public class MultiInstance {

	private final String typeName;
	private String knownAs;
	
	public StateRunnable parent;
	
	private final ArrayList<StateRunnable> instances = new ArrayList<StateRunnable>();
	 
	private final HashMap<String, StateRunnable> instanceHM = new HashMap<String, StateRunnable>();
	
	ArrayList<AbstractPostBuilder> postBuilders;
	
    @Override
	public String toString() {
		String ret = "MultiInstance of " + typeName + " " + instances.size() + " ";
		for (StateRunnable si : instances) {
			ret += si + " ";
		}
		return ret;
	}
	
	
	public MultiInstance(String tnm, String ka) {
		typeName = tnm;
		knownAs = ka;
	}

	 
	public String getKnownAs() {
		return knownAs;
	}
	
	public void add(StateInstance sr) {
		instances.add(sr);
		if (sr.getID() != null) {
			instanceHM.put(sr.getID(), sr);
		}
	}
	
	public boolean hasID(String id) {
		boolean ret = false;
		if (instanceHM.containsKey(id)) {
			ret = true;
		}
		return ret;
	}

	public StateRunnable getChildByID(String id) {
		return instanceHM.get(id);
	}




	public void initialize(StateRunnable parent) throws RuntimeError, ContentError {
		for (StateRunnable si : instances) {
			si.initialize(parent);
		}
	}

	
	public void advance(StateRunnable parent, double t, double dt) throws RuntimeError, ContentError {
		for (StateRunnable si : instances) {
			si.advance(parent, t, dt); // TODO parent or this?
		}
	}


	public void evaluate(StateRunnable parent) throws RuntimeError, ContentError {
		for (StateRunnable si : instances) {
			si.evaluate(parent);  
		}
	}
	
	public ArrayList<StateRunnable> getStateInstances() {
		return instances;
	}
	
	
	 

	
	public double[] getDoubles(String varname) throws RuntimeError {
		int n = instances.size();
		double[] ret = new double[n];
		for (int i = 0; i < n; i++) {
			StateRunnable si = instances.get(i);
			ret[i] = si.getVariable(varname);
		}
		return ret;
 	}

	public double getDouble(int i, String varname) throws RuntimeError {
		return instances.get(i).getVariable(varname);
	}

	public void setDoubles(String stateVarname, double[] wkocc) {
		for (int i = 0; i < wkocc.length; i++) {
			instances.get(i).setVariable(stateVarname, wkocc[i]);
		}
	}

	public int size() {
		return instances.size();
	}

	public void setDouble(int i, String varname, double d) {
		instances.get(i).setVariable(varname, d);
		
	}
	
	
	
	public String stateString() {
		StringBuilder sb = new StringBuilder();
		sb.append("pop " + parent.getID() + " ");
		int iunit = 0;
		for (StateRunnable ui : instances) {
			sb.append(" (" + iunit + ") " + ui.stateString());
			iunit += 1;
		}
		return sb.toString();
	}

	public void exportState(String pfx, double t, LineDisplay ld) {
		int iunit = 0;
		for (StateRunnable ui : instances) {
			ui.exportState(parent.getID() + iunit, t, ld); 
			iunit += 1;
		}	
	}

 
	public StateRunnable getChild(String snm) throws ConnectionError {
		StateRunnable ret = null;
		if (snm.startsWith("[") && snm.endsWith("]")) {
			String sint = snm.substring(1, snm.length() - 1);
			int iel = Integer.parseInt(sint);
			ret = instances.get(iel);
		} else {
			throw new ConnectionError("cant parse " + snm);
		}
		return ret;
	}

 
 


	public void setParent(StateRunnable par) {
		for (StateRunnable sr : instances) {
			sr.setParent(par);
		}
		parent = par;
	}
	
	
	public StateRunnable getParent() {
		return parent;
	}


	public StateRunnable getPredicateInstance(String lastbit) {
		String si = lastbit.substring(1, lastbit.length() - 1);
		// E.info("parsing index from predicate " + si);
		int ind = Integer.parseInt(si);
		return instances.get(ind);
	}


	public void setKnownAs(String s) {
		knownAs = s;
	}


	public StateRunnable getInstance(int idx) {
		return instances.get(idx);
	}


	public InstanceSet<StateRunnable> getInstanceSet(StateRunnable p) {
		InstanceSet<StateRunnable> ret = new InstanceSet<StateRunnable>(knownAs, p);
		ret.addAll(instances);
		return ret;
	}
	
	
	public ArrayList<StateRunnable> getInstances() {
		return instances;
	}
 
 
}

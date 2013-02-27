package org.lemsml.jlems.core.type.dynamics;

import java.util.HashMap;

import org.lemsml.jlems.core.type.Dimension;

public class DynamicsBuilder {
	
	Dynamics target;
	
	OnStart onStart;
	
	HashMap<String, StateVariable> varHM = new HashMap<String, StateVariable>();
	HashMap<String, DerivedVariable> dvHM = new HashMap<String, DerivedVariable>();
	
	public DynamicsBuilder() {
		target = new Dynamics();
	}

	public void addStateVariable(String nm, Dimension dim) {
		// TODO Auto-generated method stub
		
		StateVariable sv = new StateVariable();
		sv.setName(nm);
		sv.setDimension(dim.getName());
	 
		target.addStateVariable(sv);
		varHM.put(nm, sv);
	}
	
	
	public Dynamics getTarget() {
		return target;
	}

	public void setStateExposure(String nm, String enm) {
		varHM.get(nm).setExposure(enm);
		
	}

	public void addOnEvent(OnEvent oe) {
		 target.addOnEvent(oe);
	}

	 

	public void addDerivedVariable(String newDvName, Dimension dimension, String val) {
		DerivedVariable dv = new DerivedVariable();
		dv.setName(newDvName);
		if (dimension == null) {
			dv.setDimension("none");
		} else {
			dv.setDimension(dimension.getName());
		}
		dv.setValue(val);
		dvHM.put(newDvName, dv);
		target.addDerivedVariable(dv);
		
	}

	public void setDerivedVariableExposure(String newDvName, String s) {
		dvHM.get(newDvName).setExposure(s);
	}

	public void addTimeDerivative(String varnm, String val) {
		TimeDerivative td = new TimeDerivative();
		td.setVariable(varnm);
		td.setValue(val);
		target.addTimeDerivative(td);
	}

	public void addOnStart(String vnm, String val) {
		if (onStart == null) {
			onStart = new OnStart();
			target.addOnStart(onStart);
		}
		onStart.addStateAssignment(vnm, val);
	}

	public void addOnCondition(OnCondition oc) {
		target.addOnCondition(oc);
		
	}
	
	
}

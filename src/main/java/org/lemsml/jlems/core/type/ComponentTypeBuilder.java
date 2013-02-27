package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.dynamics.DynamicsBuilder;
import org.lemsml.jlems.core.type.dynamics.OnCondition;
import org.lemsml.jlems.core.type.dynamics.OnEvent;


public class ComponentTypeBuilder {

	ComponentType target;
	
	DynamicsBuilder dynB;
	
	
	public ComponentTypeBuilder() {
		target = new ComponentType();
 	}


	public void setName(String s) {
		target.setName(s);
	}


	public void addText(String newText) {
		target.addText(newText);
		
	}


	public ComponentType getTarget() {
		return target;
	}


	public void addParameter(String newName, Dimension dim) {
		Parameter p = new Parameter();
		p.setName(newName);
		p.setDimension(dim.getName());
		target.addParameter(p);
		
	}


	public void addExposure(String newName, Dimension dim) {
		Exposure ex = new Exposure();
		ex.setName(newName);
		ex.setDimension(dim.getName());
		target.addExposure(ex);
		
	}


	public void ensureHasRequirement(String rn, Dimension dim) throws ContentError {
		if (target.getRequirements().hasName(rn)) {
			// fine as is
		} else {
			Requirement req = new Requirement();
			req.setName(rn);
			req.setDimension(dim.getName());
			target.addRequirement(req);
		}
		
	}


	private void checkDynamics() {
		if (dynB == null) {
			dynB = new DynamicsBuilder();
			target.setDynamics(dynB.getTarget());
		}
	}
	
	
	public void addStateVariable(String nm, Dimension dim) {
		checkDynamics();
		dynB.addStateVariable(nm, dim);
	
		
	}


	public void setStateExposure(String nm, String enm) {
		dynB.setStateExposure(nm, enm);
	}


	public void addOnEvent(OnEvent oe) {
		 checkDynamics();
		 dynB.addOnEvent(oe);
		
	}


	public void addEventPort(EventPort ep) {
		 target.addEventPort(ep);
	}

 

	public void addDerivedVariable(String newDvName, Dimension dimension, String val) {
		checkDynamics();
		dynB.addDerivedVariable(newDvName, dimension, val);
	}


	public void setDerivedVariableExposure(String newDvName, String s) {
		checkDynamics();
		dynB.setDerivedVariableExposure(newDvName, s);
	}


	public void addTimeDerivative(String varnm, String val) {
		checkDynamics();
		dynB.addTimeDerivative(varnm, val);	
	}


	public void addOnStart(String vnm, String val) {
		checkDynamics();
		dynB.addOnStart(vnm, val);	
	}


	public void addOnCondition(OnCondition oc) {
		checkDynamics();
		dynB.addOnCondition(oc);	
	}

}

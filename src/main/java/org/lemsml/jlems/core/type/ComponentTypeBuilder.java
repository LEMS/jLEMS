package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.dynamics.Dynamics;
import org.lemsml.jlems.core.type.dynamics.DynamicsBuilder;
import org.lemsml.jlems.core.type.dynamics.OnCondition;
import org.lemsml.jlems.core.type.dynamics.OnEvent;
import org.lemsml.jlems.core.type.dynamics.StateVariable;


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
	public void addConstant(String name, Dimension dim, String value) {
		target.addConstant(name, dim, value);
		
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


	public void removeStateRequirements() throws ContentError {
		// during flattening, a requirement may be present (from a subcomponent)
		// that is a state variable in the same component itself. These need
		// removing
		Dynamics d = target.getDynamics();
		if (d != null) {
			for (StateVariable sv : d.getStateVariables()) {
				if (target.getRequirements().containsName(sv.getName())) {
					target.removeRequirement(sv.getName());
				}
			}
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

package org.lemsml.jlems.core.type.simulation;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentReference;
import org.lemsml.jlems.core.type.ComponentType;


@ModelElement(info="The run element provides a way to make a model runnable. It should point to the parameters that set the " +
		"step size etc. The target parameters have to be dimensionally consistent. ")
public class Run {

	@ModelProperty(info="name of the component reference that will set the component to be run")
	public String component;
	public ComponentReference r_componentRef;
	
	@ModelProperty(info="")
	public String variable;
	
	@ModelProperty(info="path to the parameter that sets the step size")
	public String increment;
	// private Valued val_increment;
	
	@ModelProperty(info="path to the parameter that sets the total span of the independent variable to be run")
	public String total;
	// private Valued val_total;

	 
	
	
	public void resolve(ComponentType r_type) throws ContentError {
	 
		if (component != null) {
			r_componentRef = r_type.getComponentRef(component);
			//val_increment = valHM.get(variable);
			//val_total = valHM.get(total);
		
			if (variable.equals("t")) {
				// OK
			} else {
				E.error("varaible in Run must be 't': Currently lems is harcoded only to accept 't' as the independent variable for runs");
			}
		}
	}


	 
	
	
	public Component getTargetComponent(Component cpt) throws ContentError {
		return cpt.getChild(component);
	}


	public double getDoubleStep(Component cpt) throws ContentError {
		 return cpt.getParamValue(increment).getDoubleValue();
	}


	public double getDoubleTotal(Component cpt) throws ContentError {
		 return cpt.getParamValue(total).getDoubleValue();
	}
	
	
}

package org.lemsml.behavior;

import java.util.HashMap;

import org.lemsml.annotation.Mat;
import org.lemsml.annotation.Mel;
import org.lemsml.expression.Valued;
import org.lemsml.type.Component;
import org.lemsml.type.ComponentReference;
import org.lemsml.type.ComponentType;
import org.lemsml.type.LemsCollection;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;


@Mel(info="The run element provides a way to make a model runnable. It should point to the parameters that set the " +
		"step size etc. The target parameters have to be dimensionally consistent. ")
public class Run {

	@Mat(info="name of the component reference that will set the component to be run")
	public String component;
	public ComponentReference r_componentRef;
	
	@Mat(info="")
	public String variable;
	
	@Mat(info="path to the parameter that sets the step size")
	public String increment;
	// private Valued val_increment;
	
	@Mat(info="path to the parameter that sets the total span of the independent variable to be run")
	public String total;
	// private Valued val_total;

	
	public String simulations;
	public LemsCollection<Component> r_simulations;
	
	
	
    public Run() {
    }

    public Run(String component, String variable, String increment, String total) {
        this.component = component;
        this.variable = variable;
        this.increment = increment;
        this.total = total;
    }


    

	
	
	public void resolve(ComponentType r_type, HashMap<String, Valued> valHM) throws ContentError {
	
		if (simulations != null) {
		
		}
		
		
		
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

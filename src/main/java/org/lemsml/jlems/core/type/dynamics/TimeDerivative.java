package org.lemsml.jlems.core.type.dynamics;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.type.Dimension;

 
@ModelElement(info="First order differential equations, functions of StateVariables and Parameters, for how StateVariables change with time. Has a variable and a value. The value is the rate of change of the variable.")
public class TimeDerivative extends AbstractStateChange {

	 
	
	public TimeDerivative() {
		super();
	}
	
    public TimeDerivative(String vnm) {
    	super(vnm);
    }

    
    public TimeDerivative(String vnm, String value) {
        this(vnm);
        this.value = value;
    }

	 
	public Dimension getStateVariableDimensionMultiplier() {
		 Dimension ret = new Dimension("per_time");
		 ret.setT(-1);
		 return ret;
	}

	public TimeDerivative makeCopy() {
		TimeDerivative ret = new TimeDerivative();
		super.copyInto(ret);
		return ret;
	}

	public String getDimensionString() {
		 Dimension ret = new Dimension("per_time");
		 return ret.getDimensionString();
	}

 

	
}

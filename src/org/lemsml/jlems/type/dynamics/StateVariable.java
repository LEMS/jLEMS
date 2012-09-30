package org.lemsml.jlems.type.dynamics;

import org.lemsml.jlems.annotation.Mat;
import org.lemsml.jlems.expression.Dimensional;
import org.lemsml.jlems.expression.Valued;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.Dimension;
import org.lemsml.jlems.type.Exposure;
import org.lemsml.jlems.type.LemsCollection;
import org.lemsml.jlems.type.Named;
import org.lemsml.jlems.util.ContentError;

 
 

public class StateVariable implements Valued  {
	
	@Mat(info="")
	public String name;
	@Mat(info="")
	public String dimension;
	public Dimension r_dimension;
	
	@Mat(info="If this variable is to be accessed from outside, it should be linked to an Exposure that is " +
			"defined in the ComponentType.")
	public String exposure;
	public Exposure r_exposure;

    public StateVariable() {
    }

    public StateVariable(String name) {
    	this.name = name;
    }
    
    
    public StateVariable(String name, Dimension dimension) {
        this.name = name;
        this.r_dimension = dimension;
        this.dimension = dimension.getName();
    }

    public StateVariable(String name, Dimension dimension, Exposure exposure) {
        this.name = name;
        this.r_dimension = dimension;
        this.dimension = dimension.getName();
        this.r_exposure = exposure;
        this.exposure = exposure.getName();
    }


   
	
	public String getName() {
		return name;
	}


    @Override
	public String toString() {
		return name + "(" + dimension + ")"; //  + hashCode();
	}

	
	public void resolve(ComponentType typ, LemsCollection<Dimension> dimensions) throws ContentError {
		Dimension d = dimensions.getByName(dimension);
		if (d != null) {
			r_dimension = d;
		//	E.info("resolved param " + name);
		} else {
			throw new ContentError("no such dimension: " + dimension);
		}
		if (exposure != null) {
			r_exposure = typ.getExposure(exposure);
		}
	}

 
	public double getValue() {
		return Double.NaN;
	}

	 
	public boolean isFixed() {
		return false;
	}

	public Dimension getDimension() {
		return r_dimension;
	}

	public Dimensional getDimensionality() {
		return r_dimension;
	}

	public boolean hasExposure() {
		boolean ret = false;
		if (r_exposure != null) {
			ret = true;
		}
		return ret;
	}

	public Named getExposure() {
		return r_exposure;
	}
	
	
}

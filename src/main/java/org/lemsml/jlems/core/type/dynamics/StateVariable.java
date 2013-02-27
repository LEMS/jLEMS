package org.lemsml.jlems.core.type.dynamics;

import org.lemsml.jlems.core.annotation.ModelProperty;
import org.lemsml.jlems.core.expression.Dimensional;
import org.lemsml.jlems.core.expression.Valued;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Dimension;
import org.lemsml.jlems.core.type.Exposure;
import org.lemsml.jlems.core.type.LemsCollection;
import org.lemsml.jlems.core.type.Named;

 
 

public class StateVariable implements Valued  {
	
	@ModelProperty(info="")
	public String name;
	
	@ModelProperty(info="")
	public String dimension;
	private Dimension r_dimension;
	
	@ModelProperty(info="If this variable is to be accessed from outside, it should be linked to an Exposure that is " +
			"defined in the ComponentType.")
	public String exposure;
	private Exposure r_exposure;
 
   
	
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

	protected void setName(String nm) {
		name = nm;
	}

	protected void setDimension(String dn) {
		dimension = dn;
	}

	public void setExposure(String enm) {
		exposure = enm;
	}

	public String getExposureName() {
		String ret = null;
		if (r_exposure != null) {
			ret = r_exposure.getName();
		} else {
			ret = exposure;
		}
		return ret;
	}


	public StateVariable makeCopy() {
		StateVariable ret = new StateVariable();
		ret.name = name;
		ret.exposure = exposure;
		ret.dimension = dimension;
		return ret;
	}
	
	
	
}

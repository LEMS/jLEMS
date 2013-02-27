package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.expression.Dimensional;
import org.lemsml.jlems.core.expression.Valued;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;

public class FinalParam implements Valued {

	public String name;
	public Dimension r_dimension;
	
	public String svalue = null;
	 
	public double dvalue = Double.NaN;
	
	public FinalParam(String nm, Dimension dim) {
		this(nm, dim, null);
	}
	
	public FinalParam(String nm, Dimension dim, String sv) {
		name = nm;
		r_dimension = dim;
		if (sv != null) {
			svalue = sv;		
			readSV();
		}
	}
	
	private void readSV() {
		if (svalue != null) {
			dvalue = Double.parseDouble(svalue);
		}
	}
	
        @Override
	public String toString() {
		if (r_dimension == null) {
			E.error("no dimension for final param: " + name + " " + svalue);
		}
		return name + "(" + r_dimension.getName() + ")";
	}
	
	public String getName() {
		return name;
	}


	public Dimension getDimension() {
		return r_dimension;
	}
	
	public String getSValue() {
		return svalue;
	}

	public void setSValue(String sv) {
		svalue = sv;
	}

	public boolean hasSValue() {
		 boolean ret = false;
		 if (svalue != null) {
			 ret = true;
		 }
		 return ret;
	}

 
	public double getValue() {
		if (Double.isNaN(dvalue)) {
			E.error("Accessed a value before it has been set? " + name + "(" + r_dimension + ")");
		}
		return dvalue;
	}

	 
	public boolean isFixed() {
		 return true;
	}

	public Dimensional getDimensionality() {
		return r_dimension;
	}

	public FinalParam makeCopy() throws ContentError {
		FinalParam ret = new FinalParam(getName(), getDimension(), getSValue());
		return ret;
	}
	
	
}

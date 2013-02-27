package org.lemsml.jlems.core.type;

 
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;


public class ParamValue implements Named {
	
	public FinalParam r_finalParam;

	public double value;

	
	
	public ParamValue(FinalParam dp) {
		r_finalParam = dp; 
		value = 0;
	}
	
	public ParamValue(FinalParam dp, double d) {
		r_finalParam = dp; 
		value = d;
	}
	
	public String getName() {
		return r_finalParam.getName();
	}

    public FinalParam getFinalParam() {
            return r_finalParam;
    }

    @Override
    public String toString() {
            return "ParamValue: "+r_finalParam +" = "+value;
    }


	
	
	public void parseValue(String s) {
		E.missing();
	}

	public void setValue(String atval, LemsCollection<Unit> units) throws ContentError, ParseError {
		
		DimensionalQuantity dq = QuantityReader.parseValue(atval, units);
		Dimension dtgt = r_finalParam.getDimension();
		
		if (dtgt == null) {
			throw new ContentError("No dimension for param " + r_finalParam);
			
		} else if (dtgt.isAny()) {
			value = dq.getDoubleValue();
			
		} else if (dq.dimensionsMatch(dtgt)) {
			value = dq.getDoubleValue();
			
		} else {			
			throw new ContentError("Can't set parameter: "+getName()+" with dimensions " + r_finalParam.getDimension() + " with string " + atval + ", " + dq);
		}
		
		
	}

	public void copyFrom(ParamValue pv) throws ContentError {
		if (r_finalParam.equals(pv.r_finalParam)) {
			value = pv.value;
		} else {
			throw new ContentError("can't copy: different dimParams?");
		}
		
	}
	
	public String stringValue() {
		return "" + value;
	}
	
	public String getDimensionName() {
		return r_finalParam.getDimension().getName();
	}

	public double getDoubleValue() {
		return value;
	}

	public double getDoubleValue(Dimension d) throws ContentError {
		double ret = 0;
		if (r_finalParam.getDimension().matches(d)) {
			ret = value; 
		} else {
			throw new ContentError("Wrong dimension for " + this + ": need " + d);
		}
		return ret;
	}
	
	 

	public void setDoubleValue(double v) {
		value = v;
	}
	
	
	
}

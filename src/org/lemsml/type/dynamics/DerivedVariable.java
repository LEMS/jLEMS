package org.lemsml.type.dynamics;

import java.util.HashMap;

import org.lemsml.annotation.Mat;
import org.lemsml.annotation.Mel;
import org.lemsml.expression.Dimensional;
import org.lemsml.expression.DoubleEvaluable;
import org.lemsml.expression.ParseError;
import org.lemsml.expression.Parser;
import org.lemsml.expression.Valued;
import org.lemsml.type.ComponentType;
import org.lemsml.type.Dimension;
import org.lemsml.type.Exposure;
import org.lemsml.type.FillableFrom;
import org.lemsml.type.Lems;
import org.lemsml.type.LemsCollection;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;
 
@Mel(info="A quantity that depends algebraically on other quantities in the model. The 'value' field can be " +
		"set to a mathematical expression, or the select field to a path expression. If the path expression " +
		"produces multiple matches, then the 'reduce' field says how these are reduced to a single value by " +
		"taking the sum or product. ")
public class DerivedVariable extends ExpressionValued implements Valued, FillableFrom {

	@Mat(info="")
	public String name;

 

	@Mat(info="")
	public String select;
	 
	
	@Mat(info="")
	public String dimension;
	public Dimension r_dimension;

	@Mat(info="Either 'add' or 'multiply'")
	public String reduce;
	
	@Mat(info="")
	public String exposure;   
	public Exposure r_exposure;

	DoubleEvaluable evaluable;
	public DerivedVariable() {
    }

    public DerivedVariable(String s) {
    	this.name = s;
    }
    
    public DerivedVariable(String s, String d) {
    	this(s);
    	dimension = d;
    }
    
    
    public DerivedVariable(String name, Dimension d, String value) {
        this(name);
        this.value = value;
        this.r_dimension = d;
        this.dimension = d.getName();

    }

    public DerivedVariable(String name, Dimension d, String value, String exposure) {
        this(name, d, value);
        this.r_exposure = new Exposure(exposure, d);
        this.exposure = r_exposure.getName();
    }

    
	
	public String getName() {
		return name;
	}
	
        @Override
	public String toString() {
		return "DerivedVariable " + name + " val=" + value;
	}


	public String getEvalString() {
		StringBuilder sb = new StringBuilder();
		if (value != null) {
			 
			sb.append("" + value + "");
			 
		} else {
			sb.append(select);
			if (reduce != null) {
				sb.append("  (REDUCE: " + reduce + ")");
			}
		}
		return sb.toString();
	}
  
	
	public void resolve(Lems lems, LemsCollection<Dimension> dimensions, ComponentType type, HashMap<String, Valued> valHM, Parser parser) throws ContentError, ParseError {
		if (select == null) {
			super.extract();
		}
		
        if (dimension != null) {
            Dimension d = dimensions.getByName(dimension);
            if (d != null) {
                r_dimension = d;
                //	E.info("resolved param " + name);
            } else {
                throw new ContentError("no such dimension: " + dimension);
            }
        }

        if (value != null) {
        	evaluable = parser.parseExpression(value);
        	evaluable.setValues(valHM);
        }

        if (select != null && select.trim().length() > 0) {
        	// TODO - could parse the select expression here into something that 
        	// can operate on the ComponentBehavior tree?
        	select = select.trim();
        } else {
        	select = null;
        }

        if (exposure != null) {
        	r_exposure = type.getExposure(exposure);
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

    
	
	public boolean hasSelection() {
		boolean ret = false;
		if (select != null) {
			ret = true;
		}
		return ret;
	}
	

	public DoubleEvaluable getEvaluable() {
		 return evaluable;
	}

	public void fillFrom(Object obj) {
		DerivedVariable dsrc = (DerivedVariable)obj;
		if (dimension == null) {
			dimension = dsrc.dimension;
		}
		if (r_dimension == null) {
			r_dimension = dsrc.r_dimension;
		}
	}

	public Dimensional getDimensionality(HashMap<String, Dimensional> dimHM) throws ContentError {
		Dimensional ret = null;
		
		if (evaluable != null) {
			ret = evaluable.getDimensionality(dimHM);

		} else if (r_dimension != null) {
			ret = r_dimension;
		} else {
			throw new ContentError("derived variable has no dimension: " + name + " " + value);
		}
		return ret;
	}
	
	public String getPath() {
		return select; // TODO or use r_select ?
	}

    public String getReduce() {
        return reduce;
    }

    public void setReduce(String reduce) {
        this.reduce = reduce;
    }



	public boolean hasExpression() {
		boolean ret = false;
		if (value != null) {
			ret = true;
		}
		return ret;
	}

	 
	public String getFunc() {
		return reduce;
	}

	public boolean hasExposure() {
		boolean ret = false;
		if (r_exposure != null) {
			ret = true;
		}
		return ret;
	}
	
	public Exposure getExposure() {
		return r_exposure;
	}

}

package org.lemsml.jlems.type.dynamics;

import java.util.HashMap;

import org.lemsml.jlems.annotation.ModelProperty;
import org.lemsml.jlems.annotation.ModelElement;
import org.lemsml.jlems.expression.Dimensional;
import org.lemsml.jlems.expression.DoubleEvaluable;
import org.lemsml.jlems.expression.ParseError;
import org.lemsml.jlems.expression.Parser;
import org.lemsml.jlems.expression.Valued;
import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.Dimension;
import org.lemsml.jlems.type.Exposure;
import org.lemsml.jlems.type.Lems;
import org.lemsml.jlems.type.LemsCollection;
 
@ModelElement(info="A quantity that depends algebraically on other quantities in the model. The 'value' field can be " +
		"set to a mathematical expression, or the select field to a path expression. If the path expression " +
		"produces multiple matches, then the 'reduce' field says how these are reduced to a single value by " +
		"taking the sum or product. ")
public class DerivedVariable extends ExpressionValued implements Valued {

	@ModelProperty(info="")
	public String name;

	@ModelProperty(info="")
	public String select;
	 	
	@ModelProperty(info="")
	public String dimension;
	public Dimension r_dimension;

	@ModelProperty(info="Either 'add' or 'multiply'. This applies if ther are multiple " +
			"matches to the path or if 'required' is false. In the latter case, for multiply mode, " +
	"multiplicative expressions in this variable behave as if the term was absent. " +
	"Additive expressions generate an error. Conversely, if set to 'add' then " +
	"additive expressions behave as if it was not there and multiplicative ones generate" +
	"and error.")
	public String reduce;
	
	@ModelProperty(info="")
	public String exposure;   
	public Exposure r_exposure;

	DoubleEvaluable evaluable;

	 
	@ModelProperty(info="Set to true if it OK for this variable to be absent. " +
			"See 'reduce' for what happens in this case")
	public boolean required = true;
	
	
	public DerivedVariable() {
    }

    public DerivedVariable(String s) {
    	this.name = s;
    }
    
     
	
	public String getName() {
		return name;
	}
	
	public String getSelect() {
		return select;
	}
	
	
        @Override
	public String toString() {
		return "DerivedVariable " + name + " val=" + value;
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

	public boolean isRequired() {
		return required;
	}

	public void setName(String nm) {
		name = nm;
	}
	
	public void setDimension(String dn) {
		dimension = dn;
	}

	public void setValue(String sv) {
		value = sv;
	}

	public void setExposure(String s) {
		exposure = s;
	}

}

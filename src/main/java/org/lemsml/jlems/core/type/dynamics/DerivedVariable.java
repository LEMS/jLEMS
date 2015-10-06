package org.lemsml.jlems.core.type.dynamics;

import java.util.HashMap;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;
import org.lemsml.jlems.core.expression.Dimensional;
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.expression.ParseTree;
import org.lemsml.jlems.core.expression.Parser;
import org.lemsml.jlems.core.expression.Valued;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Dimension;
import org.lemsml.jlems.core.type.Exposure;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.LemsCollection;
 
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

    public String description;

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
 
	 
	@ModelProperty(info="Set to true if it OK for this variable to be absent. " +
			"See 'reduce' for what happens in this case")
	public boolean required = true;
	
	
	private boolean resolved = false;
	
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
	

	
    @Override
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
                throw new ContentError("No such dimension: " + dimension+" in "+ name+" = "+value);
            }
        }

        if (value != null) {
        	parseTree = parser.parseExpression(value);
         }

        if (select != null && select.trim().length() > 0) {
        	// TODO - could parse the select expression here into something that 
        	// can operate on the StateType tree?
        	select = select.trim();
        } else {
        	select = null;
        }

        if (exposure != null) {
        	r_exposure = type.getExposure(exposure);
        }
        resolved = true;
	}

 
    @Override
	public double getValue() {
		return Double.NaN;
	}

	 
	public boolean isFixed() {
		return false;
	}

    public Dimension getDimension() {
        return r_dimension;
    }

    public String getDimensionString() {
    	if (!resolved) {
    		E.warning("Accessing dimension beforeresolving " + this);
    	}
    	if (r_dimension == null) {
    		E.warning("Null dimension in " + this);
    	}
     	String ret = r_dimension.getDimensionString();
    	return ret;
    }
	
	public boolean hasSelection() {
		boolean ret = false;
		if (select != null) {
			ret = true;
		}
		return ret;
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
		
		if (parseTree != null) {
			ret = parseTree.getDimensionality(dimHM);
		}
		
		
		if (r_dimension != null) {
			if (ret != null) {
				if (r_dimension.matches(ret)) {
					// Ok
				} else {
					throw new ContentError("Expression dimension does not match delcared dimension\n" +
							"expression gives " + ret + " but delcared is " + r_dimension);
				}
			} else {
				ret = r_dimension;
			}
		} else {
			if (ret != null) {
				r_dimension = new Dimension("", ret.getM(), ret.getL(), ret.getT(), ret.getI(), 
						ret.getK(), ret.getN(), 0);
			}
		}
		
		
		if (ret == null) {
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


	public void setExposure(String s) {
		exposure = s;
	}


	public DerivedVariable makeCopy() {
		DerivedVariable ret = new DerivedVariable();
		ret.name = name;
		ret.reduce = reduce;
		ret.select = select;
		ret.exposure = exposure;
		ret.dimension = dimension;
		ret.value = value;
		return ret;
	}

}

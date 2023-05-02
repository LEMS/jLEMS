package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.dynamics.ExpressionValued;

@ModelElement(info="A parameter that is a function of the Component's Parameters, which does not change with time. Its value " +
		"can be supplied either with the 'value' attribute that evaluates within the scope of the definition, or with the " +
		"'select' attribute which gives a path to 'primary' version of the parameter. For example, " +
		" setting select='//MembranePotential[species=channel/species]/reversal' within the appropriate context allows " +
		" a channel's reversal potential to taken from a single global setting according to its permeant ion, rather " +
		"than explicitly supplied locally.")
        
public class DerivedParameter extends ExpressionValued implements Named {

	@ModelProperty(info="The name of the derived parameter")
	public String name;

	@ModelProperty(info="The dimension, or 'none'. This should be the name of an already defined dimension element")
	public String dimension;

	@ModelProperty(info="An optional description of the derived parameter")
	public String description;
	public Dimension r_dimension;
	
	@ModelProperty(info="Path to the parameter that supplies the value. Exactly one of 'select' and 'value' is required.")
	public String select;
	
    
    public DerivedParameter() {
    }

    public DerivedParameter(String name, Dimension dimension) {
        this.name = name;
        this.dimension = dimension.getName();
        this.r_dimension = dimension;
    }
	
	public void resolve(LemsCollection<Dimension> dimensions) throws ContentError {
		if (dimension == null) {
			E.warning("no dimension for " + name);
		} else if (dimension.equals("*")) {
			r_dimension = new DeferredDimension();
		} else {
		Dimension d = dimensions.getByName(dimension);
		if (d != null) {
			r_dimension = d;
		//	E.info("resolved param " + name);
		} else {
			throw new ContentError("no such dimension: " + dimension);
		}
		}
	}

    @Override
    public String toString() {
        return "DerivedParameter{" + "name=" + name + ", dimension=" + dimension + ", r_dimension=" + r_dimension + ", select=" + select  + ", value=" + value + '}';
    }

    @Override
	public String getName() {
		return name;
	}


	public Dimension getDimension() {
		return r_dimension;
	}

	
	public String getSelect() {
		return select;
	}
 

	public String getValue() {
		return value;
	}

    public void setName(String name) {
        this.name = name;
    }

    public void setSelect(String select) {
        this.select = select;
    }
    
    public void setDimension(Dimension dim) {
        this.r_dimension = dim;
    }
    
    public DerivedParameter makeCopy() {
        DerivedParameter ret = new DerivedParameter();
        ret.name = name;
        ret.dimension = dimension;
        ret.r_dimension = r_dimension;
        ret.select = select;
        ret.value = value;
        return ret;
    }
    
}

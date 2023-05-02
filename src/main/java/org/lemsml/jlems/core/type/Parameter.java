package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;

@ModelElement(info = "A quantity, defined by name and dimension, that must be supplied when a Component of the "
+ "enclosing ComponentType is defined")
public class Parameter implements Named {

	@ModelProperty(info="The name of the parameter. This is the name of the attribute to be used when the parameter is supplied in a component definition")
	public String name;

	@ModelProperty(info="The dimension, or 'none'. This should be the name of an already defined dimension element")
	public String dimension;

	public Dimension r_dimension;

	@ModelProperty(info="An optional description of the parameter")
    public String description;

    public String exposure;


    public Parameter() {
    }

    public Parameter(String name, String dimension) {
        this.name = name;
        this.dimension = dimension;
    }

    public Parameter(String name, Dimension dimension) {
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
                throw new ContentError("no such dimension: " + dimension + " for parameter " + name + " " + description);
            }
        }
    }

    @Override
    public String toString() {
        return "Parameter{" + "name=" + name + ", dimension=" + dimension + ", r_dimension=" + r_dimension + '}';
    }

    @Override
    public String getName() {
        return name;
    }

    public Dimension getDimension() {
        return r_dimension;
    }



	protected void setName(String s) {
		 name = s;
	}

	protected void setDimension(String sd) {
		dimension = sd;
	}

}

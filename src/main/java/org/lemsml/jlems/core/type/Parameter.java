package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;

@ModelElement(info = "Sets the name an dimensionality of a parameter that must be supplied when a component is defined "
+ "curresponding to the enclosing ComponentType")
public class Parameter implements Named {
    
	@ModelProperty(info="The name of the parameter. This isthe attibute name when the parameter is supplied in a component definition")
	public String name;
	
	@ModelProperty(info="The dimension, or 'none'. This should be the ID of a deminsion element defined elsewhere")
	public String dimension;
	
	public Dimension r_dimension;
 
    public String description;
    
    public String exposure;

 
  

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

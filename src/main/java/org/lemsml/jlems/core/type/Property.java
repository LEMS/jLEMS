package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.annotation.ModelElement;
import org.lemsml.jlems.core.annotation.ModelProperty;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;

@ModelElement(info="An property on an instance of a component. Unlike a Parameter, a Property can very from " +
		"instance to instance. It should be set with an Assign element within the build specification.")
public class Property implements Named {

	@ModelProperty(info="")
	public String name;
	
	@ModelProperty(info="")
	public String dimension;
	public Dimension r_dimension;
    
	@ModelProperty(info="The defaultValue for the property must be a plain number (no units) giving the SI " +
			"magnitude of the quantity.")
	public String defaultValue;
	
	
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
	public String getName() {
		return name;
	}


	public Dimension getDimension() {
		return r_dimension;
	}

    public String getDefaultValue()
    {
        return defaultValue;
    }
    
    

 
}

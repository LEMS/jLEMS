package org.lemsml.type;

import org.lemsml.annotation.Mat;
import org.lemsml.annotation.Mel;
import org.lemsml.canonical.CanonicalElement;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;

@Mel(info = "Sets the name an dimensionality of a parameter that must be supplied when a component is defined "
+ "curresponding to the enclosing ComponentType")
public class Parameter implements Named {
    
	@Mat(info="The name of the parameter. This isthe attibute name when the parameter is supplied in a component definition")
	public String name;
	
	@Mat(info="The dimension, or 'none'. This should be the ID of a deminsion element defined elsewhere")
	public String dimension;
	
	public Dimension r_dimension;
 
    public String description;


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
                throw new ContentError("no such dimension: " + dimension);
            }
        }
    }

    public String getName() {
        return name;
    }

    public Dimension getDimension() {
        return r_dimension;
    }

    public CanonicalElement makeCanonical() {
        CanonicalElement ret = new CanonicalElement("Parameter");
        ret.add(new CanonicalElement("name", name));
        ret.add(new CanonicalElement("dimension", dimension));
        return ret;
    }
}

package org.lemsml.jlems.type;

import org.lemsml.jlems.annotation.ModelProperty;
import org.lemsml.jlems.annotation.ModelElement;
import org.lemsml.jlems.logging.E;
import org.lemsml.jlems.sim.ContentError;

@ModelElement(info = "A quantity that is made available to other component in the simulation. All variables in a Dynamics "
+ "definition are private. If other components need access to them then the definition has to explicitly link them "
+ "to an exposure defined in the component class")
public class Exposure implements Named {

    @ModelProperty(info = "name")
    public String name;
    @ModelProperty(info = "Reference to a dimension")
    public String dimension;
    public String description;
    public Dimension r_dimension;

    public Exposure() {
    }

    public Exposure(String name) {
        this.name = name;
    }

    public Exposure(String name, Dimension dimension) {
        this.name = name;
        this.r_dimension = dimension;
        this.dimension = dimension.getName();
    }

    public Exposure(String n, String d) {
        name = n;
        dimension = d;
    }

    public void resolve(LemsCollection<Dimension> dimensions) throws ContentError {
        if (dimension == null) {
            E.warning("No dimension for " + name);
        } else if (dimension.equals("*")) {
            r_dimension = new DeferredDimension();
        } else {
            Dimension d = dimensions.getByName(dimension);
            if (d != null) {
                r_dimension = d;
                //	E.info("resolved param " + name);
            } else {
                throw new ContentError("No such dimension: " + dimension + ". Existing ones: " + dimensions.listAsText());
            }
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Exposure{" + "name=" + name + ", dimension=" + dimension + ", r_dimension=" + r_dimension + '}';
    }

    public Dimension getDimension() {
        return r_dimension;
    }

    public Exposure makeCopy() {
        Exposure ret = new Exposure();
        ret.name = name;
        ret.dimension = dimension;
        ret.r_dimension = r_dimension;
        return ret;
    }
 
}

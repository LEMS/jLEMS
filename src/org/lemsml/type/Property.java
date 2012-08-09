package org.lemsml.type;

import org.lemsml.annotation.Mat;
import org.lemsml.annotation.Mel;
import org.lemsml.canonical.CanonicalElement;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;

@Mel(info="An property on an instance of a component. Unlike a Parameter, a Property can very from " +
		"instance to instance. It should be set with an Assign element within the build specification.")
public class Property implements Named {

	@Mat(info="")
	public String name;
	
	@Mat(info="")
	public String dimension;
	public Dimension r_dimension;
	
	
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
		CanonicalElement ret = new CanonicalElement("Property");
		ret.add(new CanonicalElement("name", name));
		ret.add(new CanonicalElement("dimension", dimension));
		return ret;
	}
}

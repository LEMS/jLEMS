package org.lemsml.type;

import org.lemsml.annotation.Mat;
import org.lemsml.annotation.Mel;
import org.lemsml.canonical.CanonicalElement;
import org.lemsml.util.ContentError;
import org.lemsml.util.E;

@Mel(info="A parameter that comes from other parameter values in the model rather than being set explicitly. Its value " +
		"can be supplied either with the 'value' attribute that evaluates within the scope of the definition, or with the " +
		"'select' attribute which gives a path to 'primary' version of the parameter. For example, " +
		" setting select='//MembranePotential[species=channel/species]/reversal' within the appropriate context allows " +
		" a channel's reversal potential to taken from a single global setting according to its permeant ion, rather " +
		"than explicitly supplied locally.")
        
public class DerivedParameter implements Named {

	public String name;
	public String dimension;
	public Dimension r_dimension;
	
	@Mat(info="Path to the parameter that supplies the value. Exactly one of 'select' and 'value' is required.")
	public String select;
	
	@Mat(info="Expression that supplies the value. Exactly one of 'select' and 'value' is required.")
	public String value;
	
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

	
	public String getSelect() {
		return select;
	}

	public CanonicalElement makeCanonical() {
		CanonicalElement ret = new CanonicalElement("DerivedParameter");
		ret.add(new CanonicalElement("name", name));
		ret.add(new CanonicalElement("dimension", dimension));
		return ret;
	}


	public String getValue() {
		return value;
	}
}

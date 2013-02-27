package org.lemsml.jlems.core.type.dynamics;

import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Dimension;
import org.lemsml.jlems.core.type.LemsCollection;

public class DerivedPunctateField {

	public String name;
	
	public String dimension;
	private Dimension r_dimension;
	
	public String region;
	
	public String select;
	

	public void resolve(ComponentType typ, LemsCollection<Dimension> dimensions) throws ContentError {
		Dimension d = dimensions.getByName(dimension);
	
		if (d != null) {
			r_dimension = d;
		//	E.info("resolved param " + name);
		} else {
			throw new ContentError("no such dimension: " + dimension);
		}
		 
	}

	
}

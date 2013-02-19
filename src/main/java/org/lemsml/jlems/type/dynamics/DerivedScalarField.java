package org.lemsml.jlems.type.dynamics;

import org.lemsml.jlems.sim.ContentError;
import org.lemsml.jlems.type.ComponentType;
import org.lemsml.jlems.type.Dimension;
import org.lemsml.jlems.type.LemsCollection;

public class DerivedScalarField {

	public String name;
	
	public String dimension;
	private Dimension r_dimension;
	
	public String region;
	
	public String value;
	
	 
	 
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

package org.lemsml.jlems.core.type.distribution;

import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.Dimension;
import org.lemsml.jlems.core.type.LemsCollection;
import org.lemsml.jlems.core.type.Unit;

public abstract class Distribution {

	public abstract void resolve(LemsCollection<Unit> units) throws ParseError, ContentError;

	protected Dimension r_dimension;
	
	
	public boolean dimensionsMatch(Dimension dtgt) {
		boolean ret = false;
		if (r_dimension != null && r_dimension.matches(dtgt)) {
			ret = true;
		}
		return ret;
	}
	 

}

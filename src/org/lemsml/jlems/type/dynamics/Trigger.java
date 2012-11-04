package org.lemsml.jlems.type.dynamics;

import org.lemsml.jlems.sim.ContentError;

public class Trigger extends ExpressionValued {

	
	protected boolean doneExtract;
	
	
	
	public String getCondition() throws ContentError {
		if (!doneExtract) {
			super.extract();
			doneExtract = true;
		}
		return value;
	}



	public Trigger makeCopy() {
		Trigger ret = new Trigger();
		ret.value = value;
		return ret;
	}
	
	
}

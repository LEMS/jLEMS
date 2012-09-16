package org.lemsml.type.dynamics;

import org.lemsml.util.ContentError;

public class Trigger extends ExpressionValued {

	
	protected boolean doneExtract;
	
	
	
	public String getCondition() throws ContentError {
		if (!doneExtract) {
			super.extract();
			doneExtract = true;
		}
		return value;
	}
	
	
}

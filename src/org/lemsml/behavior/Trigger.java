package org.lemsml.behavior;

import org.lemsml.util.ContentError;

public class Trigger extends ExpressionValued {

	
	boolean doneExtract;
	
	
	
	public String getCondition() throws ContentError {
		if (!doneExtract) {
			super.extract();
			doneExtract = true;
		}
		return value;
	}
	
	
}

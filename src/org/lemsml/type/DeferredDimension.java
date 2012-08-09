package org.lemsml.type;

import org.lemsml.expression.Dimensional;
import org.lemsml.util.E;

public class DeferredDimension extends Dimension {

	
	public boolean matches(Dimensional d) {
		E.missing();
		return true;
	}
	
	public boolean isAny() {
		return true; // TODO once set it doesn't match any
	}
	
}

package org.lemsml.jlems.core.type;

import org.lemsml.jlems.core.expression.Dimensional;
import org.lemsml.jlems.core.logging.E;

public class DeferredDimension extends Dimension {

	
	public boolean matches(Dimensional d) {
		E.missing();
		return true;
	}
	
	public boolean isAny() {
		return true; // TODO once set it doesn't match any
	}
	
}

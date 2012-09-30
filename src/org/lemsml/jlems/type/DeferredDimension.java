package org.lemsml.jlems.type;

import org.lemsml.jlems.expression.Dimensional;
import org.lemsml.jlems.util.E;

public class DeferredDimension extends Dimension {

	
	public boolean matches(Dimensional d) {
		E.missing();
		return true;
	}
	
	public boolean isAny() {
		return true; // TODO once set it doesn't match any
	}
	
}

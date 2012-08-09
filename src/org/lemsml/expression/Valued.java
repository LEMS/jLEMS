package org.lemsml.expression;

import org.lemsml.type.Named;

public interface Valued extends Named {

	public double getValue();
	
	public boolean isFixed();
	
	
}

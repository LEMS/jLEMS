package org.lemsml.jlems.expression;

import org.lemsml.jlems.type.Named;

public interface Valued extends Named {

	public double getValue();
	
	public boolean isFixed();
	
	
}

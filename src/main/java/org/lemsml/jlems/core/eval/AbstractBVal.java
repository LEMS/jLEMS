package org.lemsml.jlems.core.eval;

import java.util.ArrayList;

public abstract class AbstractBVal {

	public abstract boolean eval();

	public abstract void recAdd(ArrayList<DVar> val);
	
	public abstract AbstractBVal makeCopy();
	
	public abstract String toExpression();

	public abstract String toLemsExpression();
	
}

package org.lemsml.jlems.eval;

import java.util.ArrayList;

public abstract class BVal {

	public abstract boolean eval();

	public abstract void recAdd(ArrayList<DVar> val);
	
	public abstract BVal makeCopy();
}

package org.lemsml.jlems.core.eval;

import java.util.ArrayList;

public abstract class AbstractBComp extends AbstractBVal {
	
	public AbstractDVal left;
	public AbstractDVal right;
	
	public AbstractBComp(AbstractDVal dvl, AbstractDVal dvr) {
		super();
		left = dvl;
		right = dvr;
	}
	 
	 
	public void recAdd(ArrayList<DVar> val) {
		left.recAdd(val);
		right.recAdd(val);
	}
}

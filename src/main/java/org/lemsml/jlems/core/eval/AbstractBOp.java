package org.lemsml.jlems.core.eval;

import java.util.ArrayList;

public abstract class AbstractBOp extends AbstractBVal {
	
	public AbstractBVal left;
	public AbstractBVal right;
	
	public AbstractBOp(AbstractBVal dvl, AbstractBVal dvr) {
		super();
		left = dvl;
		right = dvr;
	}
	 
	 
	public void recAdd(ArrayList<DVar> val) {
		left.recAdd(val);
		right.recAdd(val);
	}
}

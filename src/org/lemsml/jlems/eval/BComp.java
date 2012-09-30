package org.lemsml.jlems.eval;

import java.util.ArrayList;

public abstract class BComp extends BVal {
	
	public DVal left;
	public DVal right;
	
	public BComp(DVal dvl, DVal dvr) {
		left = dvl;
		right = dvr;
	}
	 
	 
	public void recAdd(ArrayList<DVar> val) {
		left.recAdd(val);
		right.recAdd(val);
	}
}

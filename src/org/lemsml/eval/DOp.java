package org.lemsml.eval;

import java.util.ArrayList;

public abstract class DOp extends DVal {
	
	public DVal left;
	public DVal right;
	
	public DOp(DVal dvl, DVal dvr) {
		left = dvl;
		right = dvr;
	}
	 
	 
	public void recAdd(ArrayList<DVar> val) {
		left.recAdd(val);
		right.recAdd(val);
	}
}

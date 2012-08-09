package org.lemsml.eval;

import java.util.ArrayList;

public abstract class BOp extends BVal {
	
	public BVal left;
	public BVal right;
	
	public BOp(BVal dvl, BVal dvr) {
		left = dvl;
		right = dvr;
	}
	 
	 
	public void recAdd(ArrayList<DVar> val) {
		left.recAdd(val);
		right.recAdd(val);
	}
}

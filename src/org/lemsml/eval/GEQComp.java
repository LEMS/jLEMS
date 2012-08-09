package org.lemsml.eval;

public class GEQComp extends BComp {

	
	public GEQComp(DVal dvl, DVal dvr) {
		super(dvl, dvr);
	}

	 
	public boolean eval() {
		return (left.eval() >= right.eval());
	}
	
	
	
}

package org.lemsml.eval;

public class LEQComp extends BComp {

	
	public LEQComp(DVal dvl, DVal dvr) {
		super(dvl, dvr);
	}

	 
	public boolean eval() {
		return (left.eval() <= right.eval());
	}
	
	
	
}

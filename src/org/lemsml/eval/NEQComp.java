package org.lemsml.eval;

public class NEQComp extends BComp {

	
	public NEQComp(DVal dvl, DVal dvr) {
		super(dvl, dvr);
	}

	 
	public boolean eval() {
		return (left.eval() != right.eval());
	}
	
	
	
}

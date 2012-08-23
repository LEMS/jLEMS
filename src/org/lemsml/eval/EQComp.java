package org.lemsml.eval;

public class EQComp extends BComp {

	
	public EQComp(DVal dvl, DVal dvr) {
		super(dvl, dvr);
	}

	public EQComp makeCopy() {
		return new EQComp(left.makeCopy(), right.makeCopy());
	}
	 
	public boolean eval() {
		return (left.eval() == right.eval());
	}
	
	
	
}

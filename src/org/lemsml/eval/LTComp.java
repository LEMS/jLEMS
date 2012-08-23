package org.lemsml.eval;

public class LTComp extends BComp {

	
	public LTComp(DVal dvl, DVal dvr) {
		super(dvl, dvr);
	}

	public LTComp makeCopy() {
		return new LTComp(left.makeCopy(), right.makeCopy());
	}
	
	public boolean eval() {
		return (left.eval() < right.eval());
	}
	
	
	
}

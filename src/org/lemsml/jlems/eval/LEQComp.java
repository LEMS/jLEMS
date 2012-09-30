package org.lemsml.jlems.eval;

public class LEQComp extends BComp {

	
	public LEQComp(DVal dvl, DVal dvr) {
		super(dvl, dvr);
	}

	public LEQComp makeCopy() {
		return new LEQComp(left.makeCopy(), right.makeCopy());
	}
	
	public boolean eval() {
		return (left.eval() <= right.eval());
	}
	
	
	
}

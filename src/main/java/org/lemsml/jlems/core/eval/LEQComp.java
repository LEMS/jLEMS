package org.lemsml.jlems.core.eval;

public class LEQComp extends AbstractBComp {

	
	public LEQComp(AbstractDVal dvl, AbstractDVal dvr) {
		super(dvl, dvr);
	}

	public LEQComp makeCopy() {
		return new LEQComp(left.makeCopy(), right.makeCopy());
	}
	
	public boolean eval() {
		return (left.eval() <= right.eval());
	}
	
	
	
}

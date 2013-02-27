package org.lemsml.jlems.core.eval;

public class LTComp extends AbstractBComp {

	
	public LTComp(AbstractDVal dvl, AbstractDVal dvr) {
		super(dvl, dvr);
	}

	public LTComp makeCopy() {
		return new LTComp(left.makeCopy(), right.makeCopy());
	}
	
	public boolean eval() {
		return (left.eval() < right.eval());
	}
	
	
	
}

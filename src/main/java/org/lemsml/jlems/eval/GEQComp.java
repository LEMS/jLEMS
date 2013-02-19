package org.lemsml.jlems.eval;

public class GEQComp extends AbstractBComp {

	
	public GEQComp(AbstractDVal dvl, AbstractDVal dvr) {
		super(dvl, dvr);
	}

	public GEQComp makeCopy() {
		return new GEQComp(left.makeCopy(), right.makeCopy());
	}
	
	public boolean eval() {
		return (left.eval() >= right.eval());
	}
	
	
	
}
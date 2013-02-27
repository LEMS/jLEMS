package org.lemsml.jlems.core.eval;

public class NEQComp extends AbstractBComp {

	
	public NEQComp(AbstractDVal dvl, AbstractDVal dvr) {
		super(dvl, dvr);
	}
	
	public NEQComp makeCopy() {
		return new NEQComp(left.makeCopy(), right.makeCopy());
	}
	 
	public boolean eval() {
		return (left.eval() != right.eval());
	}
	
	
	
}

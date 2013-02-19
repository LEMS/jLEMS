package org.lemsml.jlems.eval;

public class EQComp extends AbstractBComp {

	
	public EQComp(AbstractDVal dvl, AbstractDVal dvr) {
		super(dvl, dvr);
	}

	public EQComp makeCopy() {
		return new EQComp(left.makeCopy(), right.makeCopy());
	}
	 
	public boolean eval() {
		return (left.eval() == right.eval());
	}
	
	
	
}

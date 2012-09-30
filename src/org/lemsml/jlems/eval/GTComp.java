package org.lemsml.jlems.eval;

public class GTComp extends BComp {

	
	public GTComp(DVal dvl, DVal dvr) {
		super(dvl, dvr);
	}

	public GTComp makeCopy() {
		return new GTComp(left.makeCopy(), right.makeCopy());
	}
	
	public boolean eval() {
		return (left.eval() > right.eval());
	}
	
	
	
}

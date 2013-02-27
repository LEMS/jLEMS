package org.lemsml.jlems.core.eval;

public class GTComp extends AbstractBComp {

	
	public GTComp(AbstractDVal dvl, AbstractDVal dvr) {
		super(dvl, dvr);
	}

	public GTComp makeCopy() {
		return new GTComp(left.makeCopy(), right.makeCopy());
	}
	
	public boolean eval() {
		return (left.eval() > right.eval());
	}
	
	
	
}

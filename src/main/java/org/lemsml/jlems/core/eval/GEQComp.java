package org.lemsml.jlems.core.eval;

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
	
	@Override
	public String toExpression() {
		return "("+left.toExpression() +" >= "+ right.toExpression() +")";
    }
	
	@Override
	public String toLemsExpression() {
		return "(" + left.toExpression() +" .ge. " + right.toExpression() + ")";
    }
	
	
}

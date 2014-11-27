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
	
	@Override
	public String toExpression() {
		return "("+left.toExpression() +" != "+ right.toExpression() +")";
    }
	
	@Override
	public String toLemsExpression() {
		return "("+left.toExpression() +" .ne. "+ right.toExpression() +")";
    }
}

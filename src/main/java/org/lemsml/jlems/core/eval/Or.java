package org.lemsml.jlems.core.eval;

import java.util.ArrayList;
 
public class Or extends AbstractBOp {

	
	public Or(AbstractBVal dvl, AbstractBVal dvr) {
		super(dvl, dvr);
	}
	
	
	public Or makeCopy() {
		return new Or(left.makeCopy(), right.makeCopy());
	}
	
	
	public boolean eval() {
		return left.eval() || right.eval();
	}

	
	@Override
	public String toExpression() {
		return "("+left.toExpression() +" || "+ right.toExpression() +")";
    }
	
	@Override
	public String toLemsExpression() {
		return "("+left.toExpression() +" .or. "+ right.toExpression() +")";
    }
	
        @Override
        public String toString() {
                return "("+left +" OR "+ right +")";
        }

        public String toString(String prefix, ArrayList<String> ignore) {
                return "("+left.toString() +" OR "+ right.toString() +")";
        }
	
}

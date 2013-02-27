package org.lemsml.jlems.core.eval;

import java.util.HashSet;

public class Minus extends AbstractDOp {

	public Minus(AbstractDVal dvl, AbstractDVal dvr) {
		super(dvl, dvr);
	}

	public Minus makeCopy() {
		return new Minus(left.makeCopy(), right.makeCopy());
	}
	
	public Minus makePrefixedCopy(String s, HashSet<String> stetHS) {
		return new Minus(left.makePrefixedCopy(s, stetHS), right.makePrefixedCopy(s, stetHS));
	}
	
	public double eval() {
		return left.eval() - right.eval();
	}

        @Override
        public String toExpression() {
                return "("+left.toExpression() +" - "+ right.toExpression() +")";
        }

      
	
}

package org.lemsml.jlems.core.eval;

import java.util.HashSet;

public class Power extends AbstractDOp {

	public Power(AbstractDVal dvl, AbstractDVal dvr) {
		super(dvl, dvr);
	}

	public Power makeCopy() {
		return new Power(left.makeCopy(), right.makeCopy());
	}
	
	public Power makePrefixedCopy(String s, HashSet<String> stetHS) {
		return new Power(left.makePrefixedCopy(s, stetHS), right.makePrefixedCopy(s, stetHS));
	}
	
	public double eval() {
		return Math.pow(left.eval(), right.eval());
	}

	@Override
	public String toExpression() {
		return " pow(" + left.toExpression() + ", " + right.toExpression() + ")";
    }
 
}

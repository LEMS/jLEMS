package org.lemsml.jlems.core.eval;

import java.util.HashSet;

public class Times extends AbstractDOp {

	public Times(AbstractDVal dvl, AbstractDVal dvr) {
		super(dvl, dvr);
	}
	
	
	public Times makeCopy() {
		return new Times(left.makeCopy(), right.makeCopy());
	}
	
	public Times makePrefixedCopy(String s, HashSet<String> stetHS) {
		return new Times(left.makePrefixedCopy(s, stetHS), right.makePrefixedCopy(s, stetHS));
	}

	public double eval() {
		return left.eval() * right.eval();
	}

	@Override
	public String toExpression() {
		return "("+ left.toExpression() + " * " + right.toExpression() +")";
	}

      
	
}

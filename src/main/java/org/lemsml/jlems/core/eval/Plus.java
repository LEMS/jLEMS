package org.lemsml.jlems.core.eval;

import java.util.HashSet;

public class Plus extends AbstractDOp {

	
	public Plus(AbstractDVal dvl, AbstractDVal dvr) {
		super(dvl, dvr);
	}
	
	public Plus makeCopy() {
		return new Plus(left.makeCopy(), right.makeCopy());
	}
	
	public Plus makePrefixedCopy(String s, HashSet<String> stetHS) {
		return new Plus(left.makePrefixedCopy(s, stetHS), right.makePrefixedCopy(s, stetHS));
	}
	
	public double eval() {
		return left.eval() + right.eval();
	}

	@Override
	public String toExpression() {
		return "(" + left.toExpression() +" + "+ right.toExpression() +")";
	}

        
	@Override
	public String toReversePolishExpression() {
		return left.toReversePolishExpression() + " " + right.toReversePolishExpression() +" +";
	}
}

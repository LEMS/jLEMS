package org.lemsml.jlems.core.eval;

import java.util.HashSet;

public class Divide extends AbstractDOp {

	public Divide(AbstractDVal dvl, AbstractDVal dvr) {
		super(dvl, dvr);
	}

	
	public Divide makeCopy() {
		return new Divide(left.makeCopy(), right.makeCopy());
	}
	
	 
	public Divide makePrefixedCopy(String s, HashSet<String> stetHS) {
		return new Divide(left.makePrefixedCopy(s, stetHS), right.makePrefixedCopy(s, stetHS));
	}
	
	
	public double eval() {
		return left.eval() / right.eval();
	}

	@Override
	public String toExpression() {
		return "(" + left.toExpression() + " / " + right.toExpression() + ")";
	}
	
	@Override
    public String toReversePolishExpression() {
		return left.toReversePolishExpression() + " " + right.toReversePolishExpression() +" /";
    }
}

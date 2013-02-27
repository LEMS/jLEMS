package org.lemsml.jlems.core.eval;

import java.util.HashSet;

public class Mod extends AbstractDOp {

	
	public Mod(AbstractDVal dvl, AbstractDVal dvr) {
		super(dvl, dvr);
	}
	
	
	public Mod makeCopy() {
		return new Mod(left.makeCopy(), right.makeCopy());
	}
	
	public Mod makePrefixedCopy(String s, HashSet<String> stetHS) {
		return new Mod(left.makePrefixedCopy(s, stetHS), right.makePrefixedCopy(s, stetHS));
	}
	
	public double eval() {
		return Math.round(left.eval()) % Math.round(right.eval());
	}

        @Override
        public String toExpression() {
                return "(" + left.toExpression() + " % " + right.toExpression() + ")";
        }
 
	
}

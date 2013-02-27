package org.lemsml.jlems.core.eval;

import java.util.ArrayList;

public class And extends AbstractBOp {

	
	public And(AbstractBVal dvl, AbstractBVal dvr) {
		super(dvl, dvr);
	}
	
	public And makeCopy() {
		return new And(left.makeCopy(), right.makeCopy());
	}
	
	public boolean eval() {
		return left.eval() && right.eval();
	}

        @Override
        public String toString() {
                return "("+left +" AND "+ right +")";
        }

        public String toString(String prefix, ArrayList<String> ignore) {
                return "("+left.toString() +" AND "+ right.toString() +")";
        }
	
}

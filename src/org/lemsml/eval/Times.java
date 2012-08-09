package org.lemsml.eval;

import java.util.ArrayList;

public class Times extends DOp {

	public Times(DVal dvl, DVal dvr) {
		super(dvl, dvr);
	}
	
	
	public Times makeCopy() {
		return new Times(left.makeCopy(), right.makeCopy());
	}
	
	public Times makePrefixedCopy(String s) {
		return new Times(left.makePrefixedCopy(s), right.makePrefixedCopy(s));
	}

	public double eval() {
		return left.eval() * right.eval();
	}

        @Override
        public String toString() {
                return "("+left +" * "+ right +")";
        }

        public String toString(String prefix, ArrayList<String> ignore) {
                return "("+left.toString(prefix, ignore) +" * "+ right.toString(prefix, ignore) +")";
        }

	
}

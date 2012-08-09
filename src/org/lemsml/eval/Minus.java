package org.lemsml.eval;

import java.util.ArrayList;

public class Minus extends DOp {

	public Minus(DVal dvl, DVal dvr) {
		super(dvl, dvr);
	}

	public Minus makeCopy() {
		return new Minus(left.makeCopy(), right.makeCopy());
	}
	
	public Minus makePrefixedCopy(String s) {
		return new Minus(left.makePrefixedCopy(s), right.makePrefixedCopy(s));
	}
	
	public double eval() {
		return left.eval() - right.eval();
	}

        @Override
        public String toString() {
                return "("+left +" - "+ right +")";
        }

        public String toString(String prefix, ArrayList<String> ignore) {
                return "("+left.toString(prefix, ignore) +" - "+ right.toString(prefix, ignore) +")";
        }

	
}

package org.lemsml.eval;

import java.util.ArrayList;
import java.util.HashSet;

public class Minus extends DOp {

	public Minus(DVal dvl, DVal dvr) {
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
        public String toString() {
                return "("+left +" - "+ right +")";
        }

        public String toString(String prefix, ArrayList<String> ignore) {
                return "("+left.toString(prefix, ignore) +" - "+ right.toString(prefix, ignore) +")";
        }

	
}

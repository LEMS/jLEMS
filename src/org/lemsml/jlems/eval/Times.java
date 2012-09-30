package org.lemsml.jlems.eval;

import java.util.ArrayList;
import java.util.HashSet;

public class Times extends DOp {

	public Times(DVal dvl, DVal dvr) {
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
        public String toString() {
                return "("+left +" * "+ right +")";
        }

        public String coditionalPrefixedToString(String prefix, ArrayList<String> ignore) {
                return "("+left.coditionalPrefixedToString(prefix, ignore) +" * "+ right.coditionalPrefixedToString(prefix, ignore) +")";
        }

	
}

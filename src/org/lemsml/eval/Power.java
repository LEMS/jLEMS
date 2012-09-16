package org.lemsml.eval;

import java.util.ArrayList;
import java.util.HashSet;

public class Power extends DOp {

	public Power(DVal dvl, DVal dvr) {
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
        public String toString() {
                return "("+left +" ^ "+ right +")";
        }

        public String coditionalPrefixedToString(String prefix, ArrayList<String> ignore) {
                return "("+left.coditionalPrefixedToString(prefix, ignore) +" ^ "+ right.coditionalPrefixedToString(prefix, ignore) +")";
        }

	
}

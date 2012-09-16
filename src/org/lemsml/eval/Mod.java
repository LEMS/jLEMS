package org.lemsml.eval;

import java.util.ArrayList;
import java.util.HashSet;

public class Mod extends DOp {

	
	public Mod(DVal dvl, DVal dvr) {
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
        public String toString() {
                return "(" + left + " % " + right + ")";
        }

        public String coditionalPrefixedToString(String prefix, ArrayList<String> ignore) {
                return "("+left.coditionalPrefixedToString(prefix, ignore) + " % " + right.coditionalPrefixedToString(prefix, ignore) +")";
        }
	
}

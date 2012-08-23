package org.lemsml.eval;

import java.util.ArrayList;
import java.util.HashSet;

public class Divide extends DOp {

	public Divide(DVal dvl, DVal dvr) {
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
        public String toString() {
                return "("+left +" / "+ right +")";
        }

        public String toString(String prefix, ArrayList<String> ignore) {
                return "("+left.toString(prefix, ignore) +" / "+ right.toString(prefix, ignore) +")";
        }
	
}

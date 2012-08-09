package org.lemsml.eval;

import java.util.ArrayList;

public class Power extends DOp {

	public Power(DVal dvl, DVal dvr) {
		super(dvl, dvr);
	}

	public Power makeCopy() {
		return new Power(left.makeCopy(), right.makeCopy());
	}
	
	public Power makePrefixedCopy(String s) {
		return new Power(left.makePrefixedCopy(s), right.makePrefixedCopy(s));
	}
	
	public double eval() {
		return Math.pow(left.eval(), right.eval());
	}

        @Override
        public String toString() {
                return "("+left +" ^ "+ right +")";
        }

        public String toString(String prefix, ArrayList<String> ignore) {
                return "("+left.toString(prefix, ignore) +" ^ "+ right.toString(prefix, ignore) +")";
        }

	
}

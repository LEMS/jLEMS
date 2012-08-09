package org.lemsml.eval;

import java.util.ArrayList;

public class And extends BOp {

	
	public And(BVal dvl, BVal dvr) {
		super(dvl, dvr);
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

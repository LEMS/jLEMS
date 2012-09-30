package org.lemsml.jlems.eval;

import java.util.ArrayList;
 
public class Or extends BOp {

	
	public Or(BVal dvl, BVal dvr) {
		super(dvl, dvr);
	}
	
	
	public Or makeCopy() {
		return new Or(left.makeCopy(), right.makeCopy());
	}
	
	
	public boolean eval() {
		return left.eval() || right.eval();
	}

        @Override
        public String toString() {
                return "("+left +" OR "+ right +")";
        }

        public String toString(String prefix, ArrayList<String> ignore) {
                return "("+left.toString() +" OR "+ right.toString() +")";
        }
	
}

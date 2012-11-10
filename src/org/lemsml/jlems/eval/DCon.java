package org.lemsml.jlems.eval;

import java.util.ArrayList;
import java.util.HashSet;

public class DCon extends DVal {
	
	double val;
	
	public DCon(double d) {
		val = d;
	}
	
	
	public DCon makeCopy() {
		return new DCon(val);
	}

	@Override
	public DVal makePrefixedCopy(String pfx, HashSet<String> stetHS) {
		return makeCopy();
	}
	
	public double eval() {
		return val;
	}

        @Override
        public String toExpression() {
        	return "" + val;
        }
        
        public String coditionalPrefixedToString(String prefix, ArrayList<String> ignore) {
                return toString();
        }



 
	public void recAdd(ArrayList<DVar> val) {
		 
	}


	@Override
	public void substituteVariableWith(String vnm, String pth) {
	 
	}


	@Override
	public boolean variablesIn(HashSet<String> known) {
		return true;
	}


	
}

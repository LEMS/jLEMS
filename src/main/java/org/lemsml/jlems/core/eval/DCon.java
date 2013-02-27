package org.lemsml.jlems.core.eval;

import java.util.ArrayList;
import java.util.HashSet;

public class DCon extends AbstractDVal {
	
	double val;
	
	public DCon(double d) {
		super();
		val = d;
	}
	
	
	public DCon makeCopy() {
		return new DCon(val);
	}

	@Override
	public AbstractDVal makePrefixedCopy(String pfx, HashSet<String> stetHS) {
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
		 // default is to ignore
	}


	@Override
	public void substituteVariableWith(String vnm, String pth) {
		 // default is to ignore
	}


	@Override
	public boolean variablesIn(HashSet<String> known) {
		return true;
	}


	
}

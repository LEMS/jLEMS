package org.lemsml.eval;

import java.util.ArrayList;

public class DCon extends DVal {
	
	double val;
	
	public DCon(double d) {
		val = d;
	}
	
	
	public DCon makeCopy() {
		return new DCon(val);
	}

	@Override
	public DVal makePrefixedCopy(String pfx) {
		return makeCopy();
	}
	
	public double eval() {
		return val;
	}

        @Override
        public String toString() {
                if ((int)val==val){
                        if (val<0) return "("+(int)val+")";
                        return ""+(int)val;
                }
                if (val<0) return "("+(float)val+")";
                return ""+(float)val;
        }
        
        public String toString(String prefix, ArrayList<String> ignore) {
                return toString();
        }



 
	public void recAdd(ArrayList<DVar> val) {
		 
	}


	
}

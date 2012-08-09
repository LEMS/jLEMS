package org.lemsml.eval;

import java.util.ArrayList;

import org.lemsml.type.Lems;
import org.lemsml.util.E;


public class DFunc extends DVal {
	
	String fname; 
	
	public DVal arg;
	
	public DFunc(String fnm, DVal dva) {
		fname = fnm;
		arg = dva;
	}
	
	public DFunc makeCopy() {
		return new DFunc(fname, arg.makeCopy());
	}

	@Override
	public DVal makePrefixedCopy(String pfx) {
		return new DFunc(fname, arg.makePrefixedCopy(pfx));
	}
	
	
        @Override
        public String toString() {
                return fname + "(" + arg  + ")";
        }

        public String toString(String prefix, ArrayList<String> ignore) {
                return fname + "(" + arg.toString(prefix, ignore) + ")";
        }


	 
	public double eval() {
		double ret = 0;
        Double aval = arg.eval();

		if (fname.equals("sin")) {
			ret = Math.sin(aval);
		
		} else if (fname.equals("cos")) {
			ret = Math.cos(aval);
		
		} else if (fname.equals("tan")) {
			ret = Math.tan(aval);
		
		} else if (fname.equals("ln") || fname.equals("log")) {
			ret = Math.log(aval);
		
		} else if (fname.equals("exp")) {
			ret = Math.exp(aval);
		
		} else if (fname.equals("sqrt")) {
			ret = Math.sqrt(aval);
		
		} else if (fname.equals("random")) {
			ret = aval * Lems.getRandomGenerator().nextDouble();
		} else {
			E.missing();
		}
		return ret;
	}


	 
	public void recAdd(ArrayList<DVar> val) {
		arg.recAdd(val);
	}
	 
 
}

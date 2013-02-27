package org.lemsml.jlems.core.eval;

import java.util.ArrayList;
import java.util.HashSet;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.type.Lems;


public class DFunc extends AbstractDVal {
	
	String fname; 
	
	public AbstractDVal arg;
	
	public DFunc(String fnm, AbstractDVal dva) {
		super();
		fname = fnm;
		arg = dva;
	}
	
	public DFunc makeCopy() {
		return new DFunc(fname, arg.makeCopy());
	}

	@Override
	public AbstractDVal makePrefixedCopy(String pfx, HashSet<String> stetHS) {
		return new DFunc(fname, arg.makePrefixedCopy(pfx, stetHS));
	}
	
	
        @Override
        public String toExpression() {
                return fname + "(" + arg.toExpression() + ")";
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
	 
	public void substituteVariableWith(String var, String sub) {
		arg.substituteVariableWith(var, sub);
	}

	@Override
	public boolean variablesIn(HashSet<String> known) {
		return arg.variablesIn(known);
	}
 
}

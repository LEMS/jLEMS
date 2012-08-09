package org.lemsml.run;

import java.util.HashMap;

import org.lemsml.eval.DBase;
import org.lemsml.util.RuntimeError;

public class VariableROC {

	String varname;
	DBase rateexp;
	
	public VariableROC(String name, DBase das) {
		varname = name;
		rateexp = das;
	}

	public String getVarName() {
		return varname;
	}

        public DBase getRateexp() {
            return rateexp;
        }



	public double eval(HashMap<String, Double> varHM) {
		return rateexp.eval(varHM);
	}

	public double evalptr(HashMap<String, DoublePointer> varHM) throws RuntimeError {
		return rateexp.evalptr(varHM);
	}

	public double evalptr(HashMap<String, DoublePointer> varHM, HashMap<String, DoublePointer> pvarHM) {
		return rateexp.evalptr(varHM, pvarHM);
	}

	public VariableROC makeFlat(String pfx) {
		return new VariableROC(pfx + varname, rateexp.makePrefixedCopy(pfx));
	}

	public VariableROC makeCopy() {
		 VariableROC ret = new VariableROC(varname, rateexp);
		 return ret;
	}

}

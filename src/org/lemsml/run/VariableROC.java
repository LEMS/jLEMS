package org.lemsml.run;

import java.util.HashMap;
import java.util.HashSet;

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

	public VariableROC makeFlat(String pfx, HashSet<String> stetHS) {
		return new VariableROC(pfx + varname, rateexp.makePrefixedCopy(pfx, stetHS));
	}

	public VariableROC makeCopy() {
		 VariableROC ret = new VariableROC(varname, rateexp);
		 return ret;
	}

	public String getTextExpression() {
		 return rateexp.getExpressionString();
	}

	public void substituteVariableWith(String vnm, String pth) {
		rateexp.substituteVariableWith(vnm, pth);
	}

}

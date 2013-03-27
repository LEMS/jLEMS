package org.lemsml.jlems.core.run;

import java.util.HashMap;
import java.util.HashSet;

import org.lemsml.jlems.core.eval.DoubleEvaluator;

public class VariableROC {

	String varname;
	DoubleEvaluator rateexp;
	
	protected double work;
	
	public VariableROC(String name, DoubleEvaluator das) {
		varname = name;
		rateexp = das;
	}

	public String getVariableName() {
		return varname;
	}

        public DoubleEvaluator getRateexp() {
            return rateexp;
        }


        public String getVariable() {
        	return varname;
        }
        
        
	public double eval(HashMap<String, Double> varHM) {
		return rateexp.evalD(varHM);
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

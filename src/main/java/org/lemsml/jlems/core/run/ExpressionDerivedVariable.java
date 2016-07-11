package org.lemsml.jlems.core.run;

import java.util.HashMap;
import java.util.HashSet;

import org.lemsml.jlems.core.eval.DoubleEvaluator;
 

public class ExpressionDerivedVariable {
	String varname;
	DoubleEvaluator rateexp;
	String dimension;
	
	String exposeAs;

	public ExpressionDerivedVariable() {
		// should remove other constructor and then this one.
	}

    @Override
    public String toString() {
    	String ret = "ExpressionDerivedVariable " + varname;
    	if (exposeAs != null) {
    		ret += ", exposed as " + exposeAs;
    	}
    	ret += ", expression=" + rateexp.getExpressionString();
        return ret;
    }



	public ExpressionDerivedVariable(String snm, DoubleEvaluator de, String dim) {
		varname = snm;
		rateexp = de;
		dimension = dim;
	}

	public ExpressionDerivedVariable makeFlat(String pfx, HashSet<String> indHS, String dim) {
		ExpressionDerivedVariable edv = new ExpressionDerivedVariable();
		edv.varname = pfx + varname;
		edv.exposeAs = pfx + exposeAs;
		edv.dimension = dim;
		edv.rateexp = rateexp.makePrefixedCopy(pfx, indHS);
		return edv;
	}
	
	public void setInstanceExposeAs(String s) {
		exposeAs = s;
	}
	
	public String getDimensionString() {
		return dimension;
	}
	
	public String getExposeAs() {
		return exposeAs;
	}
	
	public String getVariableName() {
		return varname;
	}

    public DoubleEvaluator getRateexp() {
            return rateexp;
    }

        

	public double eval(HashMap<String, Double> varHM) {
		return rateexp.evalD(varHM);
	}

	public double evalptr(HashMap<String, DoublePointer> varHM) throws RuntimeError {
		return rateexp.evalptr(varHM);
	}
	
	public double evalptr(HashMap<String, DoublePointer> varHM, HashMap<String, DoublePointer> v2HM) {
		return rateexp.evalptr(varHM, v2HM);
	}

	public void augment(HashMap<String, DoublePointer> variables, HashMap<String, DoublePointer> scopeVars) {
		double d = evalptr(variables, scopeVars);
		if (variables.containsKey(varname)) {
			variables.get(varname).set(d);
		} else {
			variables.put(varname, new DoublePointer(d));
		}
	}

	public void substituteVariableWith(String vnm, String pth) {
		rateexp.substituteVariableWith(vnm, pth);
	}

	public String getExpressionString() {
		return rateexp.getExpressionString();
	}

	public String getReversePolishExpressionString() {
		return rateexp.getReversePolishExpressionString();
	}

	
	
	public boolean onlyDependsOn(HashSet<String> known) {
		boolean ret = false;
		if (rateexp.variablesIn(known)) {
			ret = true;
		}
		return ret;
	}

	public boolean isTrivial() {
		boolean ret = false;
		if (rateexp.isTrivial()) {
			ret = true;
		}
		return ret;
	}

	public String getSimpleValueName() {
		return rateexp.getSimpleValueName();
	}


 
}

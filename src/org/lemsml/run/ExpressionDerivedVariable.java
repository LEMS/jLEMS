package org.lemsml.run;

import java.util.HashMap;
import java.util.HashSet;

import org.lemsml.eval.DBase;
import org.lemsml.util.RuntimeError;
 

public class ExpressionDerivedVariable {
	String varname;
	DBase rateexp;
	
	String exposeAs;

	public ExpressionDerivedVariable() {
	}

    @Override
    public String toString() {
        return "ExpressionDerivedVariable: "+ varname+" (ex: " + exposeAs + "), " + rateexp;
    }



	public ExpressionDerivedVariable(String snm, DBase db) {
		varname = snm;
		rateexp = db;
	}

	public ExpressionDerivedVariable makeFlat(String pfx, HashSet<String> indHS) {
		ExpressionDerivedVariable edv = new ExpressionDerivedVariable();
		edv.varname = pfx + varname;
		edv.exposeAs = pfx + exposeAs;
		edv.rateexp = rateexp.makePrefixedCopy(pfx, indHS);
		return edv;
	}
	
	public void setInstanceExposeAs(String s) {
		exposeAs = s;
	}
	
	public String getExposeAs() {
		return exposeAs;
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

	public boolean onlyDependsOn(HashSet<String> known) {
		boolean ret = false;
		if (rateexp.variablesIn(known)) {
			ret = true;
		}
		return ret;
	}


 
}

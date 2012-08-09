package org.lemsml.run;

import java.util.HashMap;

import org.lemsml.eval.BBase;
import org.lemsml.eval.DBase;
import org.lemsml.util.E;
import org.lemsml.util.RuntimeError;
 

public class ConditionDerivedVariable extends ExpressionDerivedVariable {

	BBase cond = null;
	DBase ifFalse = null;

    boolean debug = false;

	
	public ConditionDerivedVariable(String snm, DBase val, BBase cond, DBase ifFalse) {
		varname = snm;
		rateexp = val;
		this.cond = cond;
		this.ifFalse = ifFalse;
	}

    @Override
    public String toString() {
        return "ConditionDerivedVariable{ "+ varname+" (ex: "+exposeAs+"), "+rateexp + ", cond=" + cond + ", ifFalse=" + ifFalse + '}';
    }



    @Override
	public double eval(HashMap<String, Double> varHM) {
        boolean res = cond.eval(varHM);
        if (debug) {
        	E.info(varname +" condition "+cond+" is "+res);
        }

        if (res) {
            return rateexp.eval(varHM);
        } else {
            return ifFalse.eval(varHM);
        }
	}

    @Override
	public double evalptr(HashMap<String, DoublePointer> varHM) throws RuntimeError {
        boolean res = cond.evalptr(varHM);
        if (debug) E.info(varname +" condition "+cond+" is "+res);
        if (res) {
            return rateexp.evalptr(varHM);
        } else {
            return ifFalse.evalptr(varHM);
        }
	}
	
    @Override
	public double evalptr(HashMap<String, DoublePointer> varHM, HashMap<String, DoublePointer> v2HM) {
        boolean res = cond.evalptr(varHM, v2HM);
        if (debug) {
        	E.info(varname +" condition "+cond+" is "+res);
        }
        if (res) {
            return rateexp.evalptr(varHM, v2HM);
        } else {
            return ifFalse.evalptr(varHM, v2HM);
        }
	}

    @Override
	public void augment(HashMap<String, DoublePointer> variables, HashMap<String, DoublePointer> scopeVars) {

		double d = evalptr(variables, scopeVars);

		if (variables.containsKey(varname)) {
			variables.get(varname).set(d);
		} else {
			variables.put(varname, new DoublePointer(d));
		}
	}
 
}

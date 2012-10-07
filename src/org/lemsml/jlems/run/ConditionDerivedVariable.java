package org.lemsml.jlems.run;

import java.util.HashMap;

import org.lemsml.jlems.eval.BBase;
import org.lemsml.jlems.eval.DBase;
import org.lemsml.jlems.logging.E;
 

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
        double ret = 0.;
        if (res) {
            ret = rateexp.eval(varHM);
        } else {
            ret = ifFalse.eval(varHM);
        }
        return ret;
	}

    @Override
	public double evalptr(HashMap<String, DoublePointer> varHM) throws RuntimeError {
        boolean res = cond.evalptr(varHM);
        if (debug) {
        	E.info(varname +" condition "+cond+" is "+res);
        }
        double ret = 0.;
        if (res) {
            ret = rateexp.evalptr(varHM);
        } else {
            ret = ifFalse.evalptr(varHM);
        }
        return ret;
	}
	
    @Override
	public double evalptr(HashMap<String, DoublePointer> varHM, HashMap<String, DoublePointer> v2HM) {
        boolean res = cond.evalptr(varHM, v2HM);
        if (debug) {
        	E.info(varname +" condition "+cond+" is "+res);
        }
        double ret = 0.;
        if (res) {
            ret = rateexp.evalptr(varHM, v2HM);
        } else {
            ret = ifFalse.evalptr(varHM, v2HM);
        }
        return ret;
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

package org.lemsml.jlems.core.run;

import java.util.HashMap;

import org.lemsml.jlems.core.eval.DoubleEvaluator;

public class VariableAssignment {

	String varname;
	DoubleEvaluator valexp;
	
	public VariableAssignment(String name, DoubleEvaluator das) {
		varname = name;
		valexp = das;
	}

	public String getVarName() {
		return varname;
	}

    public DoubleEvaluator getValexp() {
            return valexp;
    }

	public double eval(HashMap<String, Double> varHM) {
		return valexp.evalD(varHM);
	}
	
	public double evalptr(HashMap<String, DoublePointer> varHM) throws RuntimeError {
		return valexp.evalptr(varHM);
	}

	public VariableAssignment makeCopy() {
		VariableAssignment ret = new VariableAssignment(varname, valexp);
		return ret;
	}
}

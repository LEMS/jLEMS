package org.lemsml.jlems.run;

import java.util.HashMap;

import org.lemsml.jlems.eval.DBase;

public class VariableAssignment {

	String varname;
	DBase valexp;
	
	public VariableAssignment(String name, DBase das) {
		varname = name;
		valexp = das;
	}

	public String getVarName() {
		return varname;
	}

    public DBase getValexp() {
            return valexp;
    }

	public double eval(HashMap<String, Double> varHM) {
		return valexp.eval(varHM);
	}
	
	public double evalptr(HashMap<String, DoublePointer> varHM) throws RuntimeError {
		return valexp.evalptr(varHM);
	}

	public VariableAssignment makeCopy() {
		VariableAssignment ret = new VariableAssignment(varname, valexp);
		return ret;
	}
}

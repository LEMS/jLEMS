package org.lemsml.eval;

import java.util.ArrayList;
import java.util.HashMap;

import org.lemsml.run.DoublePointer;
import org.lemsml.util.RuntimeError;

public class DVar extends DVal {

	String varname;
	
	double varval;
	
	
	public DVar(String s) {
		varname = s;
	}
	
	
	public DVar makeCopy() {
		return new DVar(varname);
	}
	
	public DVar makePrefixedCopy(String pfx) {
		return new DVar(pfx + varname);
	}
	
	public void set(HashMap<String, Double> valHM) {
		varval = valHM.get(varname);
	}
	
	public double eval() {
		return varval;
	}


    @Override
    public String toString() {
            return varname;
    }

    public String toString(String prefix, ArrayList<String> ignore) {
            if (ignore.contains(varname)) return varname;
            return prefix+varname;
    }

	 
	public void recAdd(ArrayList<DVar> val) {
		val.add(this);
	}

	public void setPtr(HashMap<String, DoublePointer> valptrHM) throws RuntimeError {
		if (!valptrHM.containsKey(varname)) {
			throw new RuntimeError("No such variable: " + varname + ".\n Existing variables: " + valptrHM);
		}
		
		varval = valptrHM.get(varname).get();
	}

	public void setPtr(HashMap<String, DoublePointer> valptrHM, HashMap<String, DoublePointer> rvHM) {
		if (valptrHM.containsKey(varname)) {
			varval = valptrHM.get(varname).get();
		} else {
			varval = rvHM.get(varname).get();
		} 
		// just let it throw a null pointer of not found - TODO - RuntimeError
	}
}

package org.lemsml.eval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.lemsml.run.DoublePointer;
import org.lemsml.util.E;
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
	
	public DVar makePrefixedCopy(String pfx, HashSet<String> stetHS) {
		String pnm = "";
		
		E.info("XXX making prefixed copy " + pfx + " of " + varname + " " + stetHS.contains(varname));
		if (stetHS.contains(varname)) {
			pnm = varname;
		} else {
			pnm = pfx + varname;
		}
		return new DVar(pnm);
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
	
	public void substituteVariableWith(String var, String sub) {
		if (varname.equals(var)) {
			varname = sub;
			E.info("SUB: replaced " + var + " with " + sub + " in dval ");
		}
	}


	@Override
	public boolean variablesIn(HashSet<String> known) {
		boolean ret = false;
		if (known.contains(varname)) {
			ret = true;
		}
		return ret;
	}
}

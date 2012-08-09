package org.lemsml.eval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.lemsml.run.DoublePointer;
import org.lemsml.util.RuntimeError;

public class DBase {

	DVal root;
	
	DVar[] vars;
	
	public DBase(DVal dv) {
		root = dv;
		ArrayList<DVar> val = new ArrayList<DVar>();
		root.recAdd(val);
		
		vars = val.toArray(new DVar[val.size()]);
	}

    public DVal getRoot() {
            return root;
    }

    @Override
    public String toString() {
        return "DBase{" + "root=" + root + ", vars=" + Arrays.toString(vars) + '}';
    }

    

	
	public double eval(HashMap<String, Double> valHM) {
		for (int i = 0; i < vars.length; i++) {
			vars[i].set(valHM);
		}
		return root.eval();
	}
	
	
	public double evalptr(HashMap<String, DoublePointer> valptrHM) throws RuntimeError {
		for (int i = 0; i < vars.length; i++) {
			vars[i].setPtr(valptrHM);
		}
		return root.eval();
	}
	

	public double evalptr(HashMap<String, DoublePointer> valptrHM, HashMap<String, DoublePointer> v2HM) {
		for (int i = 0; i < vars.length; i++) {
			vars[i].setPtr(valptrHM, v2HM);
		}
		return root.eval();
	}

	public DBase makeCopy() {
		DBase ret = new DBase(root.makeCopy());
		return ret;
	}

	public DBase makePrefixedCopy(String pfx) {
		DBase ret = new DBase(root.makePrefixedCopy(pfx));
		return ret;
	}
	
}

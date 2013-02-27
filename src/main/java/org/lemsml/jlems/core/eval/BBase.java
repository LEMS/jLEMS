package org.lemsml.jlems.core.eval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.lemsml.jlems.core.run.DoublePointer;
import org.lemsml.jlems.core.run.RuntimeError;

public class BBase implements BooleanEvaluator {

	AbstractBVal root;
	
	DVar[] vars;
	
	public BBase(AbstractBVal bv) {
		root = bv;
		ArrayList<DVar> val = new ArrayList<DVar>();
		root.recAdd(val);
		
		vars = val.toArray(new DVar[val.size()]);
	}

	
	public BBase makeCopy() {
		BBase ret = new BBase(root.makeCopy());
		return ret;
	}
	
	
    @Override
    public String toString() {
        return "BBase{" + "root=" + root + ", vars=" + Arrays.toString(vars) + '}';
    }

	
	public boolean evalB(HashMap<String, Double> valHM) {
		for (int i = 0; i < vars.length; i++) {
			vars[i].set(valHM);
		}
		return root.eval();
	}


	public Boolean evalptr(HashMap<String, DoublePointer> varptrHM) throws RuntimeError {
		for (int i = 0; i < vars.length; i++) {
			vars[i].setPtr(varptrHM);
		}
		return root.eval();
	}

	public Boolean evalptr(HashMap<String, DoublePointer> valptrHM, HashMap<String, DoublePointer> v2HM) {
		for (int i = 0; i < vars.length; i++) {
			vars[i].setPtr(valptrHM, v2HM);
		}
		return root.eval();
	}
	
}

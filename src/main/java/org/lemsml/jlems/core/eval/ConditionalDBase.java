package org.lemsml.jlems.core.eval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.run.DoublePointer;
import org.lemsml.jlems.core.run.RuntimeError;

public class ConditionalDBase implements DoubleEvaluator {

	 
	ArrayList<BooleanEvaluator> conditions = new ArrayList<BooleanEvaluator>();
	ArrayList<DoubleEvaluator> values = new ArrayList<DoubleEvaluator>();
	
	
	public ConditionalDBase() {
	 
	}

   
	public void addCondition(BooleanEvaluator be, DoubleEvaluator de) {
		//E.info("Added condidtion " + be + " " + de);
		conditions.add(be);
		values.add(de);
	}
	
	
	
    @Override
    public String toString() {
        return "ConditionalDBase{}";
    }

    
  
	public double evalD(HashMap<String, Double> valHM) {
		double ret = 0.;
		for (int i = 0; i < conditions.size(); i++) {
			if (conditions.get(i).evalB(valHM)) {
				ret = values.get(i).evalD(valHM);
				break;
			}
		}
		return ret;
	}
	
	
	public double evalptr(HashMap<String, DoublePointer> valptrHM) throws RuntimeError {
		double ret = 0.;
		for (int i = 0; i < conditions.size(); i++) {
			if (conditions.get(i).evalptr(valptrHM)) {
				ret = values.get(i).evalptr(valptrHM);
				break;
			}
		}
		return ret;
	}
	

	public double evalptr(HashMap<String, DoublePointer> valptrHM, HashMap<String, DoublePointer> v2HM) {
		double ret = 0.;
		E.missing();
		/*
		for (int i = 0; i < conditions.size(); i++) {
			if (conditions.get(i).evalptr(valptrHM, v2HM)) {
				ret = values.get(i).evalptr(valptrHM, v2HM);
				break;
			}
		}
		*/
		return ret;
	}

	public ConditionalDBase makeCopy() {
		ConditionalDBase ret = new ConditionalDBase();
		for (int i = 0; i < conditions.size(); i++) {
			ret.addCondition(conditions.get(i), values.get(i));
		}
		return ret;
	}

	public ConditionalDBase makePrefixedCopy(String pfx, HashSet<String> stetHS) {
		E.missing();
		return null;
	}

	public void substituteVariableWith(String vnm, String pth) {
		E.missing();
	}

	public boolean variablesIn(HashSet<String> known) {
		E.missing();
		return false;
	}


	@Override
	public String getExpressionString() {
		E.missing();
		return null;
	}

	
	
}

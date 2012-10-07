package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.DCon;
import org.lemsml.jlems.eval.DVal;
import org.lemsml.jlems.eval.DVar;
import org.lemsml.jlems.sim.ContentError;

public class VariableNode extends Node implements DoubleEvaluable {

	String svar;
	
	Valued valueSource;
	
	
	public VariableNode(String s) {
		svar = s;
	}
	
        @Override
	public String toString() {
		return svar;
	}

    
   
	 
	public double evalD(HashMap<String, Double> valHS) throws ParseError {
		double ret = 0;
		if (valHS.containsKey(svar)) {
			ret = valHS.get(svar);
		} else {
			throw new ParseError("can't eval " + svar);
		}
		return ret;
	}

	 
	public void evaluablize() {
	 
	}

 
	public void setValues(HashMap<String, Valued> valHM) throws ContentError {
		if (valHM.containsKey(svar)) {
			valueSource = valHM.get(svar);
		// 	E.info("set the value for " + svar + " to " + valueSource);
		} else {
			throw new ContentError("Unrecognized variable in expression: (" + svar + ")\nvalHM: " + valHM);
		}
		
	}
	
	public DVal makeFixed(HashMap<String, Double> fixedHM) throws ContentError {
		DVal ret = null;
		if (valueSource == null) {
			ret = new DVar(svar);
			
		} else if (valueSource.isFixed()) {
			if (fixedHM == null) {
				throw new ContentError("No fixed map supplied?");
				
			} else if (valueSource.getName() == null) {
				throw new ContentError("No name for value source when making fixed value? " + valueSource);
			} else if (!fixedHM.containsKey(valueSource.getName())) {
				throw new ContentError("Not such item in fixed map " + valueSource.getName() + " " + fixedHM);
			}
			
			
			ret = new DCon(fixedHM.get(valueSource.getName()));

		} else {
			ret = new DVar(valueSource.getName());
		}
		return ret;
	}

	 
	public Dimensional getDimensionality(HashMap<String, Dimensional> dimHM) throws ContentError {
		Dimensional ret = null;
		if (dimHM.containsKey(svar)) {
			ret = dimHM.get(svar);
		} else {
			throw new ContentError("No such variable in map: " + svar);
		}
		return ret;
	}
	

	public Dimensional evaluateDimensional(HashMap<String, Dimensional> dimHM) throws ContentError {
		Dimensional ret = null;
		if (dimHM.containsKey(svar)) {
			ret = dimHM.get(svar);
		} else {
			throw new ContentError("No such variable in map: " + svar);
		}
		return ret;
	}
	
	
}

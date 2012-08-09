package org.lemsml.expression;

import java.util.HashMap;

import org.lemsml.eval.DCon;
import org.lemsml.eval.DVal;
import org.lemsml.eval.DVar;
import org.lemsml.util.ContentError;

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

        public String getMathML(String indent, String innnerIndent) {
                return indent+"<ci> "+svar+" </ci>";
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
			throw new ContentError("Unrecognized variable in expression: (" + svar+")\nvalHM: "+valHM);
		}
		
	}
	
	public DVal makeFixed(HashMap<String, Double> fixedHM) {
		DVal ret = null;
		if (valueSource == null) {
			ret = new DVar(svar);
			
		} else if (valueSource.isFixed()) {
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

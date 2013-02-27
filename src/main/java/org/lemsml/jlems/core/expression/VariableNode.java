package org.lemsml.jlems.core.expression;

import java.util.HashMap;

import org.lemsml.jlems.core.eval.AbstractDVal;
import org.lemsml.jlems.core.eval.DCon;
import org.lemsml.jlems.core.eval.DVar;
import org.lemsml.jlems.core.sim.ContentError;

public class VariableNode extends Node implements DoubleParseTreeNode {

	String svar;
 	
	
	public VariableNode(String s) {
		super();
		svar = s;
	}
	
     
	public String toString() {
		return "{Variable: " + svar + "}";
	}
	
	public String toExpression() {
		return svar;
	}

	
	public void substituteVariables(HashMap<String, String> varHM) {
		if (varHM.containsKey(svar)) {
			svar = varHM.get(svar);
		}
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

 
   
	
	
	public AbstractDVal makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
		AbstractDVal ret = null;
	 
 			if (fixedHM != null && fixedHM.containsKey(svar)) {
				ret = new DCon(fixedHM.get(svar));
	 			} else {
	 				ret = new DVar(svar);
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
	
 

	@Override
	public void doVisit(ExpressionVisitor ev) {
		ev.visitVariable(svar);
	}
}

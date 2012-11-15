package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.DCon;
import org.lemsml.jlems.eval.DVal;
import org.lemsml.jlems.eval.DVar;
import org.lemsml.jlems.sim.ContentError;

public class VariableNode extends Node implements DoubleParseTreeNode {

	String svar;
 	
	
	public VariableNode(String s) {
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

 
   
	
	
	public DVal makeEvaluable(HashMap<String, Double> fixedHM) throws ContentError {
		DVal ret = null;
	 
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
	public ExpressionVisitor visitAll(ExpressionVisitor ev) throws ContentError {
			return ev.visitNode(null,  this,  null);
	}
}

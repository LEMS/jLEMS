package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.eval.DCon;
import org.lemsml.jlems.eval.DVal;
import org.lemsml.jlems.sim.ContentError;
 
public class ConstantNode extends Node implements DoubleParseTreeNode {
	
	String sval = null;

	double dval;
	
	public ConstantNode(String s) {
		super();
		
	
		sval = s;
		dval = Double.parseDouble(s);
	}
	
	public String toString() {
		return "{Constant: " + sval + "}";
	}
	
       
	public String toExpression() {
		return sval;
	}



	 
	public double evalD(HashMap<String, Double> valHS) {
		 return dval;
	}
 
  

	public DVal makeFixed(HashMap<String, Double> fixedHM) {
		return new DCon(dval);
	}

	public double getDoubleValue() {
		return dval;
	}

	 
	public Dimensional getDimensionality(HashMap<String, Dimensional> dimHM) throws ContentError {
		ExprDimensional ed = new ExprDimensional();
		if (dval == 0) {
			ed.setZero();
		}
		ed.setDoubleValue(dval);
		return ed;
	}
	

	public Dimensional evaluateDimensional(HashMap<String, Dimensional> dhm) throws ContentError {
		throw new ContentError("Can't use constants with  dimensions");
	}

	@Override
	public void substituteVariables(HashMap<String, String> varHM) throws ContentError {
		if (varHM.containsKey(sval)) {
			sval = varHM.get(sval);
		}
		
	}
	
	
	
}

package org.lemsml.jlems.expression;

import java.util.HashMap;

import org.lemsml.jlems.sim.ContentError;

public abstract class BooleanOperatorNode extends OperatorNode implements BooleanEvaluable {

	BooleanEvaluable leftEvaluable;
	BooleanEvaluable rightEvaluable;
	
	public BooleanOperatorNode(String s) {
		super(s);
	}


	public boolean evalB(HashMap<String, Double> valHS) throws ParseError {
 		 boolean x = (leftEvaluable != null ? leftEvaluable.evalB(valHS) : false);
		 boolean y = (rightEvaluable != null ? rightEvaluable.evalB(valHS) : false);
		 boolean ret = bool(x, y);
		 return ret;
	}
 
	
	public abstract boolean bool(boolean x, boolean y);
	
	
	private BooleanEvaluable getEvaluable(Node n) throws ParseError {
		BooleanEvaluable ret = null;
		if (n != null) {
		 if (n instanceof BooleanEvaluable) {
			 ret = (BooleanEvaluable)n;
		 } else {
			 throw new ParseError("non evaluable node " + n);
		 }
		} else {
			throw new ParseError("no node in operator? " + symbol + "left=" + left + "   right=" + right);
		}
		 return ret;
	 }


	public Dimensional getDimensionality(HashMap<String, Dimensional> dimHM) throws ContentError {
		Dimensional dl = leftEvaluable.getDimensionality(dimHM);
		Dimensional dr = rightEvaluable.getDimensionality(dimHM);
		Dimensional ret = null;
		if (dl != null && dr != null) {
			ret = dimop(dl, dr);
		} else {
			throw new ContentError("Null dimension in operator: " + dl + " " + dr + " operator: " + symbol);
		}
		return ret;
	}

	public abstract Dimensional dimop(Dimensional dl, Dimensional dr) throws ContentError;
	
		
	public void evaluablize() throws ParseError {
		leftEvaluable = getEvaluable(left);
		rightEvaluable = getEvaluable(right);
		if (leftEvaluable != null) {
			leftEvaluable.evaluablize();
		}
		if (rightEvaluable != null) {
			rightEvaluable.evaluablize();
		}
	}
	
	

	public void setValues(HashMap<String, Valued> valHM) throws ContentError {
		if (leftEvaluable != null) {
			leftEvaluable.setValues(valHM);
		}
		if (rightEvaluable != null) {
			rightEvaluable.setValues(valHM);
		}
	}


	public Dimensional evaluateDimensional(HashMap<String, Dimensional> dhm) throws ContentError {
		throw new ContentError("Can't apply boolean operations to dimensions");
	}
	

	
	
}
 